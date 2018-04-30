package profile;

import java.sql.*;

public class Database {

	private Statement stat;
	private Connection con;
	
	public void openConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:testdb");
		stat = con.createStatement();
	}
	
	
	
	
	
}