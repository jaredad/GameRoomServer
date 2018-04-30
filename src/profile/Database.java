package profile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

	private Statement stat;
	private Connection con;
	
	public Database() throws SQLException, ClassNotFoundException {
		getLargestId();
	}
	
	public void openConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:testdb");
		stat = con.createStatement();
	}
	
	public void addNewProfile(List<String> profile) throws ClassNotFoundException, SQLException {
		int id = setId(profile.get(0));
		openConnection();
		String cmd = "INSERT INTO all_players (id, name, ip, birth, bio, image)"
				+ " VALUES('" + id + "', '" + profile.get(1) + "', '" + profile.get(2) + "', '"
				+ profile.get(3) + "', '" + profile.get(4) + "', '" + profile.get(5) + "')";
		stat.execute(cmd);
		con.close();
	}
	
	public int setId(String id) throws ClassNotFoundException, SQLException {
		int id_num = 0;
		if (id.equals("0")) {
			id_num = getLargestId()+1;
		} else {
			id_num = Integer.parseInt(id);
		}
		return id_num;
	}
	
	public int getLargestId() throws ClassNotFoundException, SQLException {
		openConnection();
		int id = 0;
		String cmd = "SELECT MAX(id) FROM all_players";
		stat.execute(cmd);
		ResultSet results = stat.getResultSet();
		id = results.getInt(1);
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
	
	
}