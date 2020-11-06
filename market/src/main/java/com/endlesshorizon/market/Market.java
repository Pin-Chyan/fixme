package com.endlesshorizon.market;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import com.endlesshorizon.market.models.Instrument;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class Market {
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 5001;
	
	public static Map<String, Instrument> map = new LinkedHashMap<>();
	static String UID;

    public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client = new Socket(SERVER_IP, SERVER_PORT);
		String serverResponse = "";

		Scanner input = new Scanner(client.getInputStream());

		// keyboard inputs which are getting read in
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			try {
				serverResponse = input.nextLine();
				System.out.println(serverResponse);
				
			} catch (NoSuchElementException e) {
				System.out.println("Nothing");
				try {
					serverResponse = input.nextLine();
					System.out.println(serverResponse);
				} catch (NoSuchElementException d) {
					System.out.println("Scanner Closed");
				}
				input.close();
				return;
			}
			if (client.isConnected() && !serverResponse.isEmpty()) {
				getMarketID(serverResponse);
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			}
		}
	}

	private static void getMarketID(String response) {
		if (response.contains("UID:")) {
			final Pattern p = Pattern.compile( "(\\d{6})" );
			final Matcher m = p.matcher(response);
			if ( m.find() ) {
			  UID = m.group( 0 );
			}
		}
	}
}