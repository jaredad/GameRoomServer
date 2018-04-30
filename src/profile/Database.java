package profile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import centralCode.Main;
import centralCode.MessageType;

public class Database {

	private Statement stat;
	private Connection con;

	public void openConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:testdb");
		stat = con.createStatement();
	}

	public void handleProfile(List<String> profile) throws ClassNotFoundException, SQLException {
		if (profile.get(0).equals("0")) {
			addProfile(profile);
		} else {
			editProfile(profile);
		}
	}

	public void addProfile(List<String> profile) throws ClassNotFoundException, SQLException {
		int id = setId(profile.get(0));
		openConnection();
		String cmd = "INSERT INTO all_players (id, name, ip, birth, bio, image)" + " VALUES('" + id + "', '"
				+ profile.get(1) + "', '" + profile.get(2) + "', '" + profile.get(3) + "', '" + profile.get(4) + "', '"
				+ profile.get(5) + "')";
		stat.execute(cmd);
		Main.server.sendTo(profile.get(2),id+"", MessageType.IDUPDATE.ordinal());
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
		String cmd = "Select * From all_players;";
		stat.execute(cmd);
		ResultSet results = stat.getResultSet();
		while (results.next()) {
			for (int c = 1; c <= 6; ++c) {
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

}