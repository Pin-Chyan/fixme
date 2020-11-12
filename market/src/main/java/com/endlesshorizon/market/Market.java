package com.endlesshorizon.market;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import com.endlesshorizon.market.controllers.*;
import com.endlesshorizon.market.models.*;

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
	static Boolean setUp = false;

    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket client = new Socket(SERVER_IP, SERVER_PORT);
		String serverResponse = "";

		Scanner input = new Scanner(client.getInputStream());

		// keyboard inputs which are getting read in
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			try {
				serverResponse = input.nextLine();
				System.out.println(serverResponse);
				//System.out.println("1");
				
			} catch (NoSuchElementException e) {
				System.out.println("Nothing");
				try {
					serverResponse = input.nextLine();
					System.out.println(serverResponse);
				} catch (NoSuchElementException d) {
					System.out.println("Scanner Closed");
					// String kbip1 = keyboard.readLine();
					input.close();
					return;
				}
			}
			if (client.isConnected() && !serverResponse.isEmpty()) {
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				if (setUp == false) {
					System.out.println(UID);
					getMarketID(serverResponse);
					System.out.println(UID);
					MarketEngine.MarketEngineSetUp(UID, out);
					setUp = true;
					serverResponse = "";
				} else if (setUp == true && !serverResponse.isEmpty()) {
					System.out.println(serverResponse);
					// Thread.sleep(5000);
					MarketEngine.marketDecisions(serverResponse);
				}
				//System.out.println("2");
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