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
	List<PeopleInGame> end_toRemove  = new ArrayList<PeopleInGame>();
	
	
	
	
	final static int MAX_SIZE = 2;
	
	
	public void addToLobby(String r) throws ClassNotFoundException, SQLException {
		System.out.println("addToLobby message:" + r);
		String[] request = r.split(" ");
		List<String> request_list = Arrays.asList(request);
		System.out.println("request list: " + request_list);
		String game = request_list.get(0);
		System.out.println("@" + game + "@");
		if (game.equals("Galalite_2")) {
			galalite.add(request_list.get(1));
			checkLobby(galalite, "Galalite_2", end_galalite);
			
		} else if (game.equals("Matcher")) {
			System.out.println("Matcher: " + matcher.toString());
			matcher.add(request_list.get(1));
			checkLobby(matcher, "Matcher", end_matcher);
			System.out.println("Matcher: " + matcher);
			
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
			System.out.println("checklobby :" + game);
			System.out.println("player ids: " + lobby.get(0)+ " " + lobby.get(1));
			combatants.add(new PeopleInGame(lobby.get(0), lobby.get(1)));
			lobby.clear();
		}
	}

	public PeopleInGame findCombatant(String id, String game, String score) {
		PeopleInGame pig =  new PeopleInGame("", "");
		if (game.equals("Matcher")) {
			System.out.println("in matcher");
			for (PeopleInGame m : end_matcher) {
				System.out.println(m.getPlayer1() + " in findCombat");
				if (fcHelper(pig, m, id, score)) {
					System.out.println("findCombatant after fcHelper if, scores: " + pig.getS1() + " " + pig.getS2());
					System.out.println(end_matcher.get(0) + " endmatchertest");
					end_toRemove.add(m);
				}
			}
			remove(end_matcher);
		} else if (game.equals("Black_Hole")) {
			for (PeopleInGame b : end_blackhole) {
				if (fcHelper(pig, b, id, score)) {
					end_toRemove.add(b);
				}
			}
			remove(end_blackhole);
		} else if (game.equals("Galalite_2")) {
			for (PeopleInGame g : end_galalite) {
				if (fcHelper(pig, g, id, score)) {
					end_toRemove.add(g);
				}
			}
			remove(end_galalite);
		} else {
			for (PeopleInGame br : end_brickbreak) {
				if (fcHelper(pig, br, id, score)) {
					end_toRemove.add(br);
				}
			}
			remove(end_brickbreak);
		}
		return pig;
	}
	
	private void remove(List<PeopleInGame> end_list) {
		System.out.println("end before: " + end_list.toString());
		for (PeopleInGame m : end_toRemove) {
			end_list.remove(m);
		}
		end_toRemove.clear();
		System.out.println("end after: " + end_list.toString());
	}
	
	private boolean fcHelper(PeopleInGame pig, PeopleInGame m, String id, String score) {
		System.out.println("before if in fcHelper: " + m.getPlayer1() + " " + m.getPlayer2()+" "+id);
		if (m.getPlayer1().equals(id) || m.getPlayer2().equals(id)) {
			System.out.println("after if in fcHelper: " + m.getPlayer1() + " " + m.getPlayer2());
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
				System.out.println("fcHelper is true and done");
				return true;
			}
		}
		System.out.println("fcHelper is false and done");
		return false;
	}
	
	private void sendFoundGame(String id, String game) throws ClassNotFoundException, SQLException {
		System.out.println("send found game");
		Database database = new Database();
		Main.server.sendTo(database.getIp(id), game, MessageType.GAMEFOUND.ordinal());
	}
}
