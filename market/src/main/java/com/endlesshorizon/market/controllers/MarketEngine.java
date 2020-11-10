package com.endlesshorizon.market.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.spi.DirStateFactory.Result;

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
	private static Boolean result = true;

	public MarketEngine(String uid) {
		marketUID = uid;
	}

	public static void marketDecisions(String text) {
		// do checksum first

		marketUID = "223344";
		if (textFilter(text)) {
			int checkSum_temp = genCheckSum(command);
			// System.out.println(checkSum_temp);
			// System.out.println(checkSum);
			if (checkSum == checkSum_temp) {
				// buying or selling
				transMode();
			} else {
				System.out.println("Command was corrupted due to none same checkSum.");
			}
		}
	}

	private static void transMode() {
		switch (type.toLowerCase()) {
			case "buy":
				buyMode();
			case "sell":
				sellMode();
		}

	}

	private static void buyMode() {
		// System.out.println(instrumentName);
		for (int i = 0; i < MarketInit.instruments.size(); i++) {

			// searches through the list of instrument to find the broker searching
			// instrument
			if (MarketInit.instruments.get(i).getName().contains(instrumentName.toLowerCase())) {
				// System.out.println(MarketInit.instruments.get(i).toString());

				// checks whether the instrument which was found can actually do the required
				// transaction
				if (validBuy(MarketInit.instruments.get(i))) {
					MarketInit.instruments.get(i).subStock(quantity);

					// if no quantity of this product exist just remove it from db
					if (MarketInit.instruments.get(i).getQuantity() == 0) {
						MarketInit.instruments.remove(i);
					}
					return;
				}

				// implemented due to validBuy
				if (result == false) {
					return ;
				}
			}
		}
		
		System.out.println("The product you were looking for does not exist or no longer exist anymore");
	}

	private static Boolean validBuy(Instrument item) {
		if (!(item.getPrice() <= price)) {
			System.out.println("Your buying price is lower than the orginal selling price: " + item.getPrice());
			result = false;
			return false;
		}
		if (!(item.getQuantity() >= quantity)) {
			System.out.println("Your buying quanitity is more that the existing item quantity: " + item.getQuantity());
			result = false;
			return false;
		}
		return true;
	}

	private static void sellMode() {
		// System.out.println(instrumentName);
		for (int i = 0; i < MarketInit.instruments.size(); i++) {

			// searches through the list of instrument to find the broker searching
			// instrument
			if (MarketInit.instruments.get(i).getName().contains(instrumentName.toLowerCase())) {
				// System.out.println(MarketInit.instruments.get(i).toString());

				// checks whether the instrument which was found can actually do the required
				// transaction
				if (validSell(MarketInit.instruments.get(i))) {
					MarketInit.instruments.get(i).addStock(quantity);

					return;
				}

				// implemented due to validSell
				if (result == false) {
					return ;
				}
			}
		}
		
		System.out.println("The product you were looking for does not exist or no longer exist anymore");
	}

	private static Boolean validSell(Instrument item) {
		if (!(item.getPrice() >= price)) {
			System.out.println("Your selling price is higher than the orginal buying price: " + item.getPrice());
			result = false;
			return false;
		}
		return true;
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
			//System.out.println(command);
			return true;
		}
		System.out.println("Broker Sent Market_UID Is Invalid.");
		return false;
	}
	
	public static int genCheckSum(String message){
		int genCheckSum = 1;
        for (int i = 0; i < message.length(); i++){
			int temp = (int) (Math.floor(Math.log(message.charAt(i)) / Math.log(2))) + 1;
            genCheckSum += ((1 << temp) - 1) ^ message.charAt(i);
        }
        return genCheckSum;
    }
	
	public static void main(String[] args) throws Exception {
        try {
            MarketInit.setUpMarket();
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        MarketInit.displayInstrument();
		
		String text = "123456 sell Iron 12.2 8 223344";
		int checkSum = genCheckSum(text);
		String command = text + " " + checkSum;
		
		// incoming orders
		marketDecisions(command);
        MarketInit.displayInstrument();
		// marketDecisions(command);
        // MarketInit.displayInstrument();
	}
}
