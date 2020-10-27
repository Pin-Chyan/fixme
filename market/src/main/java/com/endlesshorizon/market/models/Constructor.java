package com.endlesshorizon.market.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.endlesshorizon.market.valids.Messages;


// format
// marketName InstrumentName Price Quantity


public class Constructor {
	private static String marketName;
	private static String instrumentName;
	private static float price;
	private static int quantity;

	public static void newMarket(Map<String, Instrument> map, String text) {
		if(Messages.validFormat(text)) {
			filterText(text);
			//dispText();
			createMarket(map);
		}
	}

	private static void createMarket(Map<String, Instrument> map) {
		Instrument item = new Instrument();
		item.setName(instrumentName);
		item.setPrice(price);
		item.setQuantity(quantity);
		map.put(marketName, item);
	}


	private static void filterText(String text) {
		String[] orders = null;

		orders = text.split("\\s+");
		marketName = orders[0].toLowerCase();
		instrumentName = orders[1].toLowerCase();
		price = Float.parseFloat(orders[2]);
		quantity = Integer.parseInt(orders[3]);
	}

	// testing for filterText
	//private static void dispText() {
	//	System.out.println("Market Name: " + marketName);
	//	System.out.println("Instrument Name: " + instrumentName);
	//	System.out.println("Price: " + price);
	//	System.out.println("Quantity: " + quantity);
	//} 

	// display by the map inside values
	//public static void displaySpec(Map<String, Instrument> map, String marketName) {

	//	System.out.println("----------------------");
	//	System.out.println("Constructor by Map");

	//	// market name still needs to be discussed how it is used
	//	System.out.println("Market Name: " + marketName);

	//	// to access map values inside you need to make use of the same instrument object then assign the map indicated searched one.
	//	Instrument item = map.get(marketName);
	//	System.out.println(item.toString());

	//	System.out.println("----------------------");

	//}


	//public static void main(String[] args) {
	//	Map<String, Instrument> map = new LinkedHashMap<>();
	//	String text = "Endrizon Stocks 12.3 8";

	//	newMarket(map, text);

	//	//displaySpec(map, marketName);


	//	//Instrument hatDisplay = map.get("Endrizon");
	//	//System.out.println(hatDisplay.toString());
	//}
}
