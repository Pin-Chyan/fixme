package com.endlesshorizon.market;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Market {
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 5001;

    public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client = new Socket(SERVER_IP, SERVER_PORT);

		BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

		// keyboard inputs which are getting read in
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		while (true) {
			String serverResponse = input.readLine();
			System.out.println(serverResponse);

			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			System.out.print("> ");
			String command = keyboard.readLine();
			
			out.println(command);

			if (command.equals("exit")) {
				break;
			}
	
		}

		client.close();
		System.exit(0);
	}
}