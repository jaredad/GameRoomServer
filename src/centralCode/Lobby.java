package centralCode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import profile.Database;

public class Lobby {
	
	List<String> galalite = new ArrayList<String>();
	List<String> matcher = new ArrayList<String>();
	List<String> brickbreak = new ArrayList<String>();
	List<String> blackhole = new ArrayList<String>();
	
	List<PeopleInGame> end_galalite = new ArrayList<PeopleInGame>();
	List<PeopleInGame> end_matcher = new ArrayList<PeopleInGame>();
	List<PeopleInGame> end_brickbreak = new ArrayList<PeopleInGame>();
	List<PeopleInGame> end_blackhole = new ArrayList<PeopleInGame>();
	
	
	final static int MAX_SIZE = 2;
	
	
	public void addToLobby(String r) throws ClassNotFoundException, SQLException {
		System.out.println(r);
		String[] request = r.split(" ");
		List<String> request_list = Arrays.asList(request);
		System.out.println(request_list);
		String game = request_list.get(0);
		if (game.equals("Galalite_2")) {
			galalite.add(request_list.get(1));
			checkLobby(galalite, "Galalite_2", end_galalite);
			
		} else if (game.equals("Matcher")) {
			System.out.println(matcher);
			matcher.add(request_list.get(1));
			checkLobby(matcher, "Matcher", end_matcher);
			System.out.println(matcher);
			
		} else if (game.equals("Black_Hole")) {
			blackhole.add(request_list.get(1));
			checkLobby(blackhole, "Black_Hole", end_blackhole);
			
		} else {
			brickbreak.add(request_list.get(1));
			checkLobby(brickbreak, "Brick_Break", end_brickbreak);
		}
	}
	private void checkLobby(List<String> lobby, String game, List<PeopleInGame> combatants) throws ClassNotFoundException, SQLException {
		if (lobby.size() >= MAX_SIZE) {
			System.out.println("max size reached");
			sendFoundGame(lobby.get(0), game);
			sendFoundGame(lobby.get(1), game);
			System.out.println("before");
			System.out.println(lobby.get(0)+ " " + lobby.get(1));
			combatants.add(new PeopleInGame(lobby.get(0), lobby.get(1)));
			lobby.clear();
		}
	}

	public PeopleInGame findCombatant(String id, String game, String score) {
		PeopleInGame pig =  new PeopleInGame("", "");
		if (game.equals("Matcher")) {
			System.out.println("in matcher");
			for (PeopleInGame m : end_matcher) {
				System.out.println(m.getPlayer1() + "//////");
				if (fcHelper(pig, m, id, score)) {
					pig = pigSet(m);
					System.out.println(end_matcher.get(0) + "endmatchertest");
					end_matcher.remove(m);
				}
			}
		} else if (game.equals("Black_Hole")) {
			for (PeopleInGame b : end_blackhole) {
				if (fcHelper(pig, b, id, score)) {
					pig = pigSet(b);
					end_blackhole.remove(b);
				}
			}
		} else if (game.equals("Galalite_2")) {
			for (PeopleInGame g : end_galalite) {
				if (fcHelper(pig, g, id, score)) {
					pig = pigSet(g);
					end_galalite.remove(g);
				}
			}
		} else {
			for (PeopleInGame br : end_brickbreak) {
				if (fcHelper(pig, br, id, score)) {
					pig = pigSet(br);
					end_brickbreak.remove(br);
				}
			}
		}
		return pig;
	}
	
	private boolean fcHelper(PeopleInGame pig, PeopleInGame m, String id, String score) {
		System.out.println(m.getPlayer1() + " " + m.getPlayer2()+"fffffffffff "+id);
		if (m.getPlayer1().equals(id) || m.getPlayer2().equals(id)) {
			System.out.println(m.getPlayer1() + " " + m.getPlayer2());
			if (m.getPlayer1().equals(id)) {
				m.oneIsDone();
				m.setS1(score);
			} else {
				m.twoIsDone();
				m.setS2(score);
			}
			pig.setP1(m.getPlayer1());
			pig.setP2(m.getPlayer2());
			if (m.done1()) {
				pig.oneIsDone();
				pig.setS1(m.getS1());
			}
			if (m.done2()) {
				pig.twoIsDone();
				pig.setS2(m.getS2());
			}
			if (pig.done1() && pig.done2()) {
				return true;
			}
		}
		return false;
	}
	
	private PeopleInGame pigSet(PeopleInGame m) {
		PeopleInGame pig = new PeopleInGame(m.getPlayer1(), m.getPlayer2());
		pig.setS1(m.getS1());
		pig.setS2(m.getS2());
		pig.oneIsDone();
		pig.twoIsDone();
		return pig;
	}
	
	private void sendFoundGame(String id, String game) throws ClassNotFoundException, SQLException {
		System.out.println("send found game");
		Database database = new Database();
		Main.server.sendTo(database.getIp(id), game, MessageType.GAMEFOUND.ordinal());
	}
}
