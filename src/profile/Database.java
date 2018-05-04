package profile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import centralCode.Main;
import centralCode.MessageType;

public class Database {

	private Statement stat;
	private Connection con;

	public Database() throws ClassNotFoundException, SQLException {
		getTableValues();
	}

	public void openConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:testdb");
		stat = con.createStatement();
	}

	public void handleProfile(List<String> profile) throws ClassNotFoundException, SQLException {
		if (profile.get(0).equals("0")) {
			int id = setId(profile.get(0));
			addProfile(id, profile);
			addStats(id);
		} else {
			editProfile(profile);
		}
	}

	public void addStats(int id) throws ClassNotFoundException, SQLException {
		openConnection();
		String cmd = "INSERT INTO stats (id, wins, losses, ties, gamerscore)" + " VALUES('" + id + "', '" + 0 + "', '"
				+ 0 + "', '" + 0 + "', '" + 0 + "')";
		stat.execute(cmd);
		con.close();
	}

	public void addProfile(int id, List<String> profile) throws ClassNotFoundException, SQLException {
		openConnection();
		String cmd = "INSERT INTO all_players (id, name, ip, birth, bio, image)" + " VALUES('" + id + "', '"
				+ profile.get(1) + "', '" + profile.get(2) + "', '" + profile.get(3) + "', '" + profile.get(4) + "', '"
				+ profile.get(5) + "')";
		stat.execute(cmd);
		Main.server.sendTo(profile.get(2), id + "", MessageType.IDUPDATE.ordinal());
		con.close();
	}

	public void editProfile(List<String> info) throws ClassNotFoundException, SQLException {
		openConnection();
		String cmd = "UPDATE items SET name = '" + info.get(1) + "', ip = '" + info.get(2) + "', birth = '"
				+ info.get(3) + "', bio = '" + info.get(4) + "' WHERE id = '" + info.get(0) + "';";
		con.close();
	}

	public int setId(String id) throws ClassNotFoundException, SQLException {
		int id_num = 0;
		id_num = getLargestId() + 1;
		return id_num;
	}

	public int getLargestId() throws ClassNotFoundException, SQLException {
		openConnection();
		int id = 0;
		String cmd = "SELECT MAX(id) FROM all_players";
		stat.execute(cmd);
		ResultSet results = stat.getResultSet();
		id = results.getInt(1);
		con.close();
		return id;
	}

	public void getTableValues() throws SQLException, ClassNotFoundException {
		openConnection();
		String cmd = "Select * From stats;";
		stat.execute(cmd);
		ResultSet results = stat.getResultSet();
		while (results.next()) {
			for (int c = 1; c <= 5; ++c) {
				System.out.println(results.getString(c));
			}
		}
		con.close();
	}

	public String getIp(String id) throws SQLException, ClassNotFoundException {
		openConnection();
		String cmd = "Select ip From all_players WHERE id = '" + id + "';";
		stat.execute(cmd);
		ResultSet results = stat.getResultSet();
		String ip = results.getString(1);
		con.close();
		System.out.println(ip);
		return ip;
	}

	public String getId(String ip) throws SQLException, ClassNotFoundException {
		openConnection();
		String cmd = "Select id From all_players WHERE ip = '" + ip + "';";
		stat.execute(cmd);
		ResultSet results = stat.getResultSet();
		String id = results.getString(1);
		con.close();
		return id;
	}

	public void gameOutcome(String player1, String player2, int result) throws SQLException, ClassNotFoundException {
		if (result == 0) {
			playersTie(player1, player2);
		} else if (result == 1) {
			playerOneWin(player1, player2);
		} else if (result == 2) {
			playerTwoWin(player1, player2);
		}
		updateGamerscore(player1, player2);
	}

	public void updateGamerscore(String player1, String player2) throws ClassNotFoundException, SQLException {
		String cmd = "";
		double newScore = newScore(player1);
		openConnection();
		System.out.println("NEW SCORE: " + newScore);
		cmd = "UPDATE stats SET gamerscore = '" + newScore + "' WHERE id = '" + player1 + "';";
		stat.execute(cmd);
		con.close();
		newScore = newScore(player2);
		openConnection();
		cmd = "UPDATE stats SET gamerscore = '" + newScore + "' WHERE id = '" + player2 + "';";
		stat.execute(cmd);
		con.close();
	}

	public void playersTie(String player1, String player2) throws ClassNotFoundException, SQLException {
		String cmd = "";
		int ties = getTies(player1);
		int newTies = addOne(ties);
		openConnection();
		cmd = "UPDATE stats SET ties = '" + newTies + "' WHERE id = '" + player1 + "';";
		stat.execute(cmd);
		con.close();
		ties = getTies(player2);
		newTies = addOne(ties);
		openConnection();
		cmd = "UPDATE stats SET ties = '" + newTies + "' WHERE id = '" + player2 + "';";
		stat.execute(cmd);
		con.close();
	}

	public void playerOneWin(String player1, String player2) throws ClassNotFoundException, SQLException {
		openConnection();
		String cmd = "";
		int wins = getWins(player1);
		int newWins = addOne(wins);
		openConnection();
		cmd = "UPDATE stats SET wins = '" + newWins + "' WHERE id = '" + player1 + "';";
		stat.execute(cmd);
		con.close();
		int losses = getLosses(player2);
		int newLosses = addOne(losses);
		openConnection();
		cmd = "UPDATE stats SET losses = '" + newLosses + "' WHERE id = '" + player2 + "';";
		stat.execute(cmd);
		con.close();
	}

	public void playerTwoWin(String player1, String player2) throws ClassNotFoundException, SQLException {
		openConnection();
		String cmd = "";
		int wins = getWins(player2);
		int newWins = addOne(wins);
		openConnection();
		cmd = "UPDATE stats SET wins = '" + newWins + "' WHERE id = '" + player2 + "';";
		stat.execute(cmd);
		con.close();
		int losses = getLosses(player1);
		int newLosses = addOne(losses);
		openConnection();
		cmd = "UPDATE stats SET losses = '" + newLosses + "' WHERE id = '" + player1 + "';";
		stat.execute(cmd);
		con.close();
	}

	public int getWins(String player) throws SQLException, ClassNotFoundException {
		openConnection();
		String cmd = "Select wins From stats WHERE id = '" + player + "';";
		stat.execute(cmd);
		ResultSet results = stat.getResultSet();
		int wins = results.getInt(1);
		con.close();
		return wins;
	}

	public int getLosses(String player) throws SQLException, ClassNotFoundException {
		openConnection();
		String cmd = "Select losses From stats WHERE id = '" + player + "';";
		stat.execute(cmd);
		ResultSet results = stat.getResultSet();
		int losses = results.getInt(1);
		con.close();
		return losses;
	}

	public int getTies(String player) throws SQLException, ClassNotFoundException {
		openConnection();
		String cmd = "Select ties From stats WHERE id = '" + player + "';";
		stat.execute(cmd);
		ResultSet results = stat.getResultSet();
		int ties = results.getInt(1);
		con.close();
		return ties;
	}

	public int addOne(int wins) {
		int newNum = wins + 1;
		return newNum;
	}

	public double newScore(String player) throws NumberFormatException, ClassNotFoundException, SQLException {
		int wins = getWins(player);
		System.out.println("WINS " + wins);
		int losses = getLosses(player);
		System.out.println("LOSSES " + losses);
		double score = (double) wins / (wins + losses) * (100);
		System.out.println(score);
		return score;
	}

	public String getStats(String id) throws ClassNotFoundException, SQLException {
		openConnection();
		String cmd = "Select * From stats WHERE id = '" + id + "';";
		stat.execute(cmd);
		ResultSet results = stat.getResultSet();
		String stats = "";
		for (int c = 1; c <= 5; ++c) {
			stats = stats + results.getString(c) + " ";
		}
		stats = stats.substring(0, stats.length() - 1);
		con.close();
		return stats;
	}

	public String getGamerscores() throws ClassNotFoundException, SQLException {
		openConnection();
		List<String> gamerscores = gamerscoreList();
		List<String> name = covertIdToName(idList());
		List<String> finalList = new ArrayList<String>();
		for (int i = 0; i < name.size(); i++) {
			finalList.add(name.get(i));
			finalList.add(gamerscores.get(i));
		}
		System.out.println(finalList);
		String message = "";
		for (int i = 0; i < finalList.size(); i++) {
			message = message + " " + finalList.get(i);
		}
		System.out.println(message);
		return message;
	}

	public List<String> covertIdToName(List<String> id) throws SQLException{
		List<String> nameList = new ArrayList<>();
		for (int i = 0; i < id.size();i++) {
			String cmd = "Select name from all_players Where id = '" + id.get(i) + "';";
			stat.execute(cmd);
			ResultSet results = stat.getResultSet();
			nameList.add(results.getString(1));
		}
		return nameList;
	}

	public List<String> gamerscoreList() throws SQLException {
		String cmd = "Select gamerscore from stats";
		stat.execute(cmd);
		ResultSet results = stat.getResultSet();
		List<String> gamerscores = new ArrayList<String>();
		while (results.next()) {
			gamerscores.add(Double.toString(results.getDouble(1)));
		}
		return gamerscores;
	}

	public List<String> idList() throws SQLException {
		String cmd = "Select id from stats";
		stat.execute(cmd);
		ResultSet results = stat.getResultSet();
		List<String> id = new ArrayList<String>();
		while (results.next()) {
			id.add(Integer.toString(results.getInt(1)));
		}
		return id;
	}

}