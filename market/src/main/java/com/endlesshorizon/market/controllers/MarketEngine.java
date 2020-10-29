package com.endlesshorizon.market.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.endlesshorizon.market.models.Instrument;

// Market engine is for router use
// handle trandsactions

// received message from router
// Broker_UID type instrument price amount market_UID Checksum

public class MarketEngine {
	// used for storing bought instument quantity
	private static List<String> clientsUID = new ArrayList<String>();
	private static List<String> clientsListings = new ArrayList<String>();

	// storing of broker information easier access
	private static String clientUID;
	private static String command;
	private static String marketUID;
	private static String type;
	private static String instrumentName;
	private static float price;
	private static int quantity;

	// used for validating if the message recieved from the router was not corrupted compared to the first initail checkSum given by broker
	private static int checkSum;

	public MarketEngine(String uid) {
		marketUID = uid;
	}

	public static void marketDecisions(String text) {
		// do checksum first

		marketUID = "223344";
		if (textFilter(text)) {
			int checkSum_temp = genCheckSum(command);
			System.out.println(checkSum_temp);
			System.out.println(checkSum);
			if (checkSum == checkSum_temp) {
				// buying or selling
				transMode();
			} else {
				System.out.println("Command was corrupted due to none same checkSum.");
			}
		}
	}

	private static void transMode() {

	}

	private static Boolean textFilter(String text) {
		String[] orders = null;

		orders = text.split("\\s+");
		if (marketUID.contains(orders[5])) {
			clientUID = orders[0];
			type = orders[1];
			instrumentName = orders[2];
			price = Float.parseFloat(orders[3]);
			quantity = Integer.parseInt(orders[4]);
			checkSum = Integer.parseInt(orders[6]);
			command = orders[0] + " " + orders[1] + " " + orders[2] + " " + orders[3] + " " + orders[4] + " " + orders[5];
			System.out.println(command);
			return true;
		}
		System.out.println("Broker Sent Market_UID Is Invalid.");
		return false;
	}

	public static void main(String[] args) {


		String text = "123456 buy stock 12.8 8 223344";
		int checkSum = genCheckSum(text);
		String command = text + " " + checkSum;

		// incoming orders
		marketDecisions(command);
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
