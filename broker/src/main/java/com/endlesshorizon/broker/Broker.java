package com.endlesshorizon.broker;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.server.UID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.BooleanControl;

import com.endlesshorizon.broker.utils.Prefixes;
import com.endlesshorizon.broker.valids.Messages;

import lombok.val;

public class Broker {
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 5000;
	static Boolean connected = false;
	static String UID;
	static int checkSum;

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket client = new Socket(SERVER_IP, SERVER_PORT);
		boolean validated = true;
		boolean uidRan = false;
		String serverResponse = "";

		BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

		// keyboard inputs which are getting read in
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		while (true) {
			if (validated && !uidRan){
				serverResponse = input.readLine();
				System.out.println(serverResponse);
			}
			if (client.isConnected()) {
				getBrokerID(serverResponse);
				uidRan = true;
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				System.out.print(Prefixes.FM_BCON + "UID:" + UID +"_>");
				String command = keyboard.readLine();
		
				if (command.equals("exit")) {
					break;
				}
				//365573 978970 iron-0.82-13=shoes-7.59-11
				if (Messages.validFormat(command)) {
					validated = true;
					checkSum = genCheckSum(UID + " " + command);
					out.println(UID + " " + command + " " + checkSum);
					if (command.contains("list")){
						Boolean completedAction = false;
						String newResponse = "" ;
						String prevResponse = "";
						System.out.println("Getting list please wait...");
						while (Boolean.toString(completedAction).equals("false")){
							prevResponse = newResponse;
							newResponse = input.readLine();
							if (!newResponse.isEmpty() && !newResponse.equals(prevResponse)){
								if (newResponse.length() >= 40 && newResponse.substring(31,39).equals("recieved")){
									System.out.println(newResponse);
									System.out.println("Loading List...");
								} else if (newResponse.contains("none")){
									System.out.println(newResponse.substring(14));
									completedAction = true;
								} else {
									String InstList = newResponse.substring(14);
									String[] InsArr = null;
									InsArr = InstList.split("=");
									String[] cmds = null;
									cmds = command.split("\\s+");
									
									System.out.println("========Market["+cmds[1]+"]========");
									for (int jj = 0; jj < InsArr.length; jj++) {
										String[] ValArr = null;
										ValArr = InsArr[jj].split("-");
										System.out.println("__________"+ValArr[0]+"__________");
										System.out.println("Price: "+ValArr[1]);
										System.out.println("Quantity: "+ValArr[2]);
									}
									completedAction = true;
								}
							}
						}
					} else if (command.equals("markets")){
						Boolean completedAction = false;
						String newResponse = "" ;
						String prevResponse = "";
						System.out.println("Getting list please wait...");
						while (Boolean.toString(completedAction).equals("false")){
							prevResponse = newResponse;
							newResponse = input.readLine();
							if (!newResponse.isEmpty() && !newResponse.equals(prevResponse)){
								if (newResponse.contains("recieved")){
									System.out.println(newResponse);
									System.out.println("Loading List...");
								} else if (newResponse.contains("none")){
									System.out.println(newResponse.substring(14));
									completedAction = true;
								} else {
									String MarksList = newResponse;
									String[] MarksArr = null;
									MarksArr = MarksList.split("=");
									String[] cmds = null;
									
									System.out.println("========Markets========");
									for (int jj = 0; jj < MarksArr.length; jj++) {
										System.out.println(MarksArr[jj]);
									}
									completedAction = true;
								}
							}
						}
					} else {
						Boolean completedAction = false;
						String newResponse = "" ;
						String prevResponse = "";
						System.out.println("Running transaction please wait...");
						while (Boolean.toString(completedAction).equals("false")){
							prevResponse = newResponse;
							newResponse = input.readLine();
							if (!newResponse.isEmpty() && !newResponse.equals(prevResponse)){
								if (newResponse.substring(31,39).equals("recieved")){
									System.out.println(newResponse);
									System.out.println("Awaiting status.");
									System.out.println("Loading...");
								} else if (newResponse.contains("Your buying price is lower than")){
									System.out.println(newResponse.substring(14));
									completedAction = true;
								} else if (newResponse.contains("was a success")){
									System.out.println(newResponse.substring(14));
									completedAction = true;
								} else if (newResponse.contains("does not exist or no longer exists anymore")){
									System.out.println(newResponse.substring(14));
									completedAction = true;
								} else if (newResponse.contains("Your selling price is higher than")){
									System.out.println(newResponse.substring(14));
									completedAction = true;
								} else if (newResponse.contains("You sold")){
									System.out.println(newResponse.substring(14));
									completedAction = true;
								} else if (newResponse.contains("Your buying quanitity is more")){
									System.out.println(newResponse.substring(14));
									completedAction = true;
								} else if (newResponse.contains("The MarketUID you gave does not exist")){
									System.out.println(newResponse.substring(14));
									completedAction = true;
								} else {
									System.out.println("Unkown response: "+newResponse);
									completedAction = true;
								}
							}
						}
					}
				} else {
					validated = false;
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
