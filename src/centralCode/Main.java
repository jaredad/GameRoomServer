package centralCode;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Platform;
import network.Server;
import profile.Database;

public class Main {

	public static Server server;
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Database data = new Database();
		System.out.println("Hello World!");
		server = new Server();
		listen();
	}

	private static void listen() {

		new Thread(() -> {
			System.out.println("listening");
			try {
				System.out.println("listening");
				server.listen();
			} catch (IOException e) {
				//Platform.runLater(() -> bad.badNews(e.getMessage()));
				e.printStackTrace();
			}
		}).start();
	}
	
	
}
