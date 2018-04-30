package centralCode;

import java.sql.SQLException;

import javafx.application.Platform;
import profile.Database;
import profile.Profile;

public class MessageHandler {

	public Profile profileHandler;
	public Lobby lobby;
	
	public MessageHandler() {
		profileHandler = new Profile();
		lobby = new Lobby();
		
	}

	public void handleMessage(String message) throws ClassNotFoundException, SQLException {
		int spaceIndex = message.indexOf(" ");
		int ord = Integer.parseInt(message.substring(0, spaceIndex));
		message = message.substring(spaceIndex + 1);
		if (ord == MessageType.PROFILE.ordinal()) {
			handleProfile(message);
		} else if (ord == MessageType.ONLINEPLAYER.ordinal()) {
			//updateProfileView();
		} else if (ord == MessageType.REQUESTGAME.ordinal()) {
			System.out.println("made it to lobby");
			lobby.addToLobby(message);
		} else if (ord == MessageType.SENDSCORE.ordinal()) {
			
				final String m = message;
				Platform.runLater(() -> {
					try {
						findScoreSender(m);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
		}
	}

	private void handleProfile(String profile) throws ClassNotFoundException, SQLException {
		profileHandler.deserialize(profile);
	}

	private void findScoreSender(String message) throws ClassNotFoundException, SQLException {
		Database database = new Database();
		String[] info = message.split(" ");
		String id = database.getId(info[0]);
		System.out.println(info[1]);
		PeopleInGame pig = lobby.findCombatant(id, info[2], info[1]);
		System.out.println(pig.getPlayer1() + " " + pig.getPlayer2());
		if (pig.done1() && pig.done2()) {
			sendScores(pig.getPlayer1(), pig.getS2());
			sendScores(pig.getPlayer2(), pig.getS1());
		}
	}
	
	private void sendScores(String id, String score) throws ClassNotFoundException, SQLException {
		Platform.runLater(() -> {
			Database database = new Database();
			System.out.println("hey");
			try {
				Main.server.sendTo(database.getIp(id), "test"+score, MessageType.SENDSCORE.ordinal());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}
