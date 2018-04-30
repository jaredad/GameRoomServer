package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Platform;

public class Server {

	private final int portNum = 8888;
	private ServerSocket server;

	public Server() {
		try {
			server = new ServerSocket(portNum);
		} catch (IOException e) {
			//bad.badNews(e.getMessage());
			e.printStackTrace();
		}
	}

	public void listen() throws IOException {
		try {
			while (true) {
				System.out.println("working on it");
				Socket s = server.accept();
				newClient(s);
			}
		} catch (Exception e) {
			//Platform.runLater(() -> bad.badNews(e.getMessage()));
			e.printStackTrace();
		}
	}

	private void newClient(Socket s) throws IOException { // Dr. Ferrer sockDemo
		try {
			BufferedReader responses = new BufferedReader(new InputStreamReader(s.getInputStream()));
			StringBuilder sb = new StringBuilder();
			while (!responses.ready()) {
			}
			while (responses.ready()) {
				sb.append(responses.readLine());
			}
			System.out.println(sb.toString());
			//dealWithInput(sb.toString().toString());
			s.close();

		} catch (Exception e) {
			//Platform.runLater(() -> bad.badNews(e.getMessage()));
			e.printStackTrace();
		}
	}

	public void sendTo(String host, String message, int ord) { // Dr. Ferrer sockDemo
		new Thread(() -> {
			try {
				Socket target = new Socket(host, portNum);
				send(target, ord + message);
				target.close();
			} catch (Exception e) {
				//Platform.runLater(() -> bad.badNews(e.getMessage()));
				e.printStackTrace();
			}
		}).start();
	}

	private void send(Socket target, String message) throws IOException { // Dr. Ferrer sockDemo
		PrintWriter sockout = new PrintWriter(target.getOutputStream());
		sockout.println(message);
		sockout.flush();
	}
}
