package com.endlesshorizon.broker;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Broker {
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 5000;

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client = new Socket(SERVER_IP, SERVER_PORT);

		BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		while (true) {
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			System.out.print("> ");
			String command = keyboard.readLine();
			
			out.println(command);

			if (command.equals("exit")) {
				break;
			}
	
			String serverResponse = input.readLine();
			System.out.println(serverResponse);
		}

		client.close();
		System.exit(0);
	}
}
