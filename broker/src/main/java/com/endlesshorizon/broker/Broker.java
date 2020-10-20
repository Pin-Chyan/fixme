package com.endlesshorizon.broker;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.endlesshorizon.broker.utils.Prefixes;
import com.endlesshorizon.broker.valids.Messages;

public class Broker {
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 5000;
	static Boolean connected = false;
	static String UID;

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client = new Socket(SERVER_IP, SERVER_PORT);

		BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

		// keyboard inputs which are getting read in
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		while (true) {
			String serverResponse = input.readLine();
			System.out.println(serverResponse);
			if (client.isConnected()) {
				getBrokerID(serverResponse);
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				System.out.print(Prefixes.FM_BCON + "UID:" + UID +"_>");
				String command = keyboard.readLine();
	
				if (Messages.validFormat(command)) {
					out.println(command);
				}
				out.println();
	
				if (command.equals("exit")) {
					break;
				}
			}
		}

		client.close();
		System.exit(0);
	}
	
	private static void getBrokerID(String response) {
		final Pattern p = Pattern.compile( "(\\d{6})" );
		final Matcher m = p.matcher(response);
		if ( m.find() ) {
		  UID = m.group( 0 );
		}
	}
}
