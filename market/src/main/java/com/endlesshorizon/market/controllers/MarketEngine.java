package com.endlesshorizon.market.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.endlesshorizon.market.models.Constructor;
import com.endlesshorizon.market.models.Instrument;

// Market engine is for router use
// handle trandsactions

public class MarketEngine {
	private static List<String> clients = new ArrayList<String>();
	private static String marketUID;
	private static String type;
	private static String instrumentName;
	private static float price;
	private static int quantity;

	public static void marketDecisions(Map<String, Instrument> map,String text) {
		// recieve text from server/router which is the brokers command
		if (textFilter(text)) {
			
			// buying or selling
			transMode(map);
		}
		// once variables are assigned in
		//dispText();
		
		
		// does the handling of transactions
		//if (requiredAmount(map)) {
			//System.out.println("Requirements met");
		//}
	}

	private static Boolean textFilter(String text) {
		String[] orders = null;

		orders = text.split("\\s+");
		marketUID = "223344";
		if (marketUID.contains(orders[1])) {
			clients.add(orders[0]);
			type = orders[2].toLowerCase();
			instrumentName = orders[3].toLowerCase();
			price = Float.parseFloat(orders[4]);
			quantity = Integer.parseInt(orders[5]);
			System.out.println("values assigned.");
			return true;
		}
		System.out.println("broker sent market_id is invalid.");
		return false;
	}

	private static void transMode(Map<String, Instrument> map) {
		switch (type) {
			case "buy":
				if (requiredAmount(map)) {
					purchaseStock(map);
					System.out.println("Requirements met");
				}
				break;
			case "sell":
					System.out.println("Broken");
				break;
		}
	}
	
	private static Boolean requiredAmount(Map<String, Instrument> map) {
		Instrument item = map.get(marketUID);
		
		if (!(item.getName().equals(instrumentName))) {
			//System.out.println(item.getName() + "\n" + instrumentName);
			System.out.println("Incorrect Instrument Name.");
			return false;
		}
		if (!(item.getPrice() <= price)) {
			System.out.println("Given Buy/Sell price is lower than orginated price.");
			return false;
		}
		if (!(item.getQuantity() >= quantity)) {
			System.out.println("Exceeded the amount of total quantity: " + item.getQuantity());
			return false;
		}
		return true;
	}

	private static void purchaseStock(Map<String, Instrument> map) {
		Instrument item = map.get(marketUID);
		float subTotal;

		item.subStock(quantity);
		subTotal = price - item.getPrice();
		if (subTotal >= 0) {
			System.out.println("You Paid Extra: " + subTotal);
		}
		map.put(marketUID, item);
	}

	// testing if variables were sett in
	//private static void dispText() {
	//	System.out.println("Market UID: " + marketUID);
	//	System.out.println("Type: " + type);
	//	System.out.println("Instrument Name: " + instrumentName);
	//	System.out.println("Price: " + price);
	//	System.out.println("Quantity" + quantity);
	//}

	// display map values
	public static void displaySpec(Map<String, Instrument> map, String marketUID) {

		System.out.println("----------------------");
		System.out.println("Constructor by Map");

		// Market UID still needs to be discussed how it is used
		System.out.println("Market UID: " + marketUID);

		// to access map values inside you need to make use of the same instrument object then assign the map indicated searched one.
		Instrument item = map.get(marketUID);
		System.out.println(item.toString());

		System.out.println("----------------------");
		
	}

	public static void mapFeedback(Map<String, Instrument> map) {
		System.out.println();
	}


	public static void main(String[] args) {
		// all the markets are created and put into the map
		Map<String, Instrument> map = new LinkedHashMap<>();

		// creating the market in the constructor.
		String text = "223344 stocks 12.3 8";

		// creating a buy or sell order.
		//String text_R = "Buy stocks 122.3 7";
		String text_R = "422122 223344 Buy stocks 12.3 4";

		// creation of the market
		Constructor.newMarket(map, text);
		marketUID = "223344"; // temp to use displaySpec
		displaySpec(map, marketUID);

		// does the commands which is requested from the broker
		marketDecisions(map, text_R);
		//Constructor.displaySpec(map, marketUID);
		displaySpec(map, marketUID);

		//check the amoung of clients are there
		//for(int i=0;i<clients.size();i++){
		//	System.out.println(clients.get(i));
		//} 
	}
}
