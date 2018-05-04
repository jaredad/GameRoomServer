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
			// updateProfileView();
		} else if (ord == MessageType.REQUESTGAME.ordinal()) {
			System.out.println("made it to lobby");
			lobby.addToLobby(message);
		} else if (ord == MessageType.SENDSCORE.ordinal()) {
			try {
				findScoreSender(message);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (ord == MessageType.REQUESTPROFILES.ordinal()) {
			getProfile(message);
		} else if (ord == MessageType.REQUESTGAMERSCORES.ordinal()) {
			getGamerscores(message);
		}
	}

	private void handleProfile(String profile) throws ClassNotFoundException, SQLException {
		profileHandler.deserialize(profile);
	}

	private void findScoreSender(String message) throws ClassNotFoundException, SQLException {
		Database database = new Database();
		String[] info = message.split(" ");
		String id = database.getId(info[0]);
		System.out.println("findScoreSender info[1]: " + info[1]);
		PeopleInGame pig = lobby.findCombatant(id, info[2], info[1]);
		System.out.println("findScoreSender player one and two: " + pig.getPlayer1() + " " + pig.getPlayer2());
		System.out.println("findScoreSender pigs done: " + pig.done1() + " " + pig.done2());
		if (pig.done1() && pig.done2()) {
			sendScores(pig.getPlayer1(), pig.getS2() + " " + pig.getS1());
			sendScores(pig.getPlayer2(), pig.getS1() + " " + pig.getS2());
			compareScores(pig.getS1(), pig.getS2(), pig.getPlayer1(), pig.getPlayer2());
		}
	}

	private void sendScores(String id, String score) throws ClassNotFoundException, SQLException {
		Database database = new Database();
		System.out.println("hey");
		try {
			Main.server.sendTo(database.getIp(id), score, MessageType.SENDSCORE.ordinal());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void compareScores(String score1, String score2, String player1, String player2) throws ClassNotFoundException, SQLException {
		Database database = new Database();
		if (Integer.parseInt(score1) > Integer.parseInt(score2)) {
			database.gameOutcome(player1, player2, 1);
		} else if (Integer.parseInt(score2)>Integer.parseInt(score1)) {
			database.gameOutcome(player1, player2, 2);
		} else {
			database.gameOutcome(player1, player2, 0);
		}
	}
	
	private void getProfile(String message) {
		Database database;
		try {
			database = new Database();
			Main.server.sendTo(database.getIp(message), database.getStats(message), MessageType.REQUESTPROFILES.ordinal());
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void getGamerscores(String message) {
		Database database;
		try {
			database = new Database();
			Main.server.sendTo(database.getIp(message), database.getGamerscores(), MessageType.REQUESTGAMERSCORES.ordinal());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
