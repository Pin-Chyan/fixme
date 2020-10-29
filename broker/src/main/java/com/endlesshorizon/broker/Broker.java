package com.endlesshorizon.broker;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.server.UID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.endlesshorizon.broker.utils.Prefixes;
import com.endlesshorizon.broker.valids.Messages;

public class Broker {
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 5000;
	static Boolean connected = false;
	static String UID;
	static int checkSum;

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
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
					checkSum = genCheckSum(UID + " " + command);
					out.println(UID + " " + command + " " + checkSum);
					//Thread.sleep(5000);
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
		if (response.contains("UID:")) {
			final Pattern p = Pattern.compile( "(\\d{6})" );
			final Matcher m = p.matcher(response);
			if ( m.find() ) {
			  UID = m.group( 0 );
			}
		}
	}

	public static int genCheckSum(String message){
        int genCheckSum = 1;
        for (int i = 0; i < message.length(); i++){
            int temp = (int) (Math.floor(Math.log(message.charAt(i)) / Math.log(2))) + 1;
            genCheckSum += ((1 << temp) - 1) ^ message.charAt(i);
        }
        return genCheckSum;
    }
}
