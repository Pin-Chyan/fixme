package com.endlesshorizon.market.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import com.endlesshorizon.market.models.Constructor;
import com.endlesshorizon.market.models.Instrument;

// Market engine is for router use
// handle trandsactions

public class MarketEngine {
	private static String marketName;
	private static String type;
	private static String instrumentName;
	private static float price;
	private static int quantity;

	public static void marketDecisions(Map<String, Instrument> map,String text) {
		// recieve text from server/router which is the brokers command
		textFilter(text);
		// once variables are assigned in
		//dispText();

		// buying or selling
		transMode(map);

		// does the handling of transactions
		//if (requiredAmount(map)) {
			//System.out.println("Requirements met");
		//}
		System.out.println("ignored");
	}

	private static void textFilter(String text) {
		String[] orders = null;

		orders = text.split("\\s+");
		marketName = "endrizon";
		type = orders[0].toLowerCase();
		instrumentName = orders[1].toLowerCase();
		price = Float.parseFloat(orders[2]);
		quantity = Integer.parseInt(orders[3]);
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
		Instrument item = map.get(marketName);
		
		if (!(item.getName().equals(instrumentName))) {
			System.out.println(item.getName() + "\n" + instrumentName);
			System.out.println("Incorrect Instrument Name.");
			return false;
		}
		if (!(item.getPrice() <= price)) {
			System.out.println("Given Buy/Sell price is lower than orginated price.");
			return false;
		}
		if (!(item.getQuantity() >= quantity)) {
			System.out.println("oh fuck");
			return false;
		}
		return true;
	}

	private static void purchaseStock(Map<String, Instrument> map) {
		Instrument item = map.get(marketName);
		float subTotal;

		item.subStock(quantity);
		subTotal = price - item.getPrice();
		if (subTotal >= 0) {
			System.out.println("You Paid Extra: " + subTotal);
		}
		map.put(marketName, item);
	}

	// testing if variables were sett in
	//private static void dispText() {
	//	System.out.println("Market Name: " + marketName);
	//	System.out.println("Type: " + type);
	//	System.out.println("Instrument Name: " + instrumentName);
	//	System.out.println("Price: " + price);
	//	System.out.println("Quantity" + quantity);
	//}

	// display map values
	public static void displaySpec(Map<String, Instrument> map, String marketName) {

		System.out.println("----------------------");
		System.out.println("Constructor by Map");

		// market name still needs to be discussed how it is used
		System.out.println("Market Name: " + marketName);

		// to access map values inside you need to make use of the same instrument object then assign the map indicated searched one.
		Instrument item = map.get(marketName);
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
		String text = "Endrizon stocks 12.3 8";

		// creating a buy or sell order.
		String text_R = "Buy stocks 122.3 7";

		Constructor.newMarket(map, text);
		marketName = "endrizon"; // temp to use displaySpec
		displaySpec(map, marketName);
		marketDecisions(map, text_R);
		//Constructor.displaySpec(map, marketName);
		displaySpec(map, marketName);
	}
}
