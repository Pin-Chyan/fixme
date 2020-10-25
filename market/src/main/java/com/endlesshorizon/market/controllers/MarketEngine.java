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

		// does the handling of transactions
	}

	private static void textFilter(String text) {
		String[] orders = null;

		orders = text.split("\\s+");
		marketName = "Endrizon";
		type = orders[0];
		instrumentName = orders[1];
		price = Float.parseFloat(orders[2]);
		quantity = Integer.parseInt(orders[3]);
	}
	
	private static void requiredAmount() {

	}

	// testing if variables were sett in
	//private static void dispText() {
	//	System.out.println("Market Name: " + marketName);
	//	System.out.println("Type: " + type);
	//	System.out.println("Instrument Name: " + instrumentName);
	//	System.out.println("Price: " + price);
	//	System.out.println("Quantity" + quantity);
	//}

	public static void main(String[] args) {
		Map<String, Instrument> map = new LinkedHashMap<>();
		String text = "Endrizon Stocks 12.3 8";
		String text_R = "Buy Stocks 12.3 8";

		Constructor.newMarket(map, text);
		marketDecisions(map, text_R);
	}
}
