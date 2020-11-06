package com.endlesshorizon.market;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import com.endlesshorizon.market.models.Instrument;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class Market {
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 5001;
	
	public static Map<String, Instrument> map = new LinkedHashMap<>();
	static String UID;

    public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client = new Socket(SERVER_IP, SERVER_PORT);
		
		while (true) {
			String serverResponse = input.readLine();
			System.out.println(serverResponse);
			if (client.isConnected()) {
				getMarketID(serverResponse);
				System.out.print("> ");
			}
		}

		client.close();
		System.exit(0);
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