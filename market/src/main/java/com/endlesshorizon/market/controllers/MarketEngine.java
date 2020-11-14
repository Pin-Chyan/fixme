package com.endlesshorizon.market.controllers;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.spi.DirStateFactory.Result;

import com.endlesshorizon.market.models.Instrument;
import com.endlesshorizon.market.utils.MarketUtils;

// Market engine is for router use
// handle trandsactions

// received message from router
// Broker_UID type instrument price amount market_UID Checksum

public class MarketEngine {
	// storing of broker information easier access
	private static String clientUID;
	private static String marketUID;
	private static String type;
	private static String instrumentName;
	private static float price;
	private static int quantity;

	// used for checkSum
	private static String command;
	
	// returning message back to the router containing brokerUID/clientUID and marketUID with the message which will be sent with
	private static String reply;
	
	
	// used for validating if the message recieved from the router was not corrupted compared to the first initail checkSum given by broker
	private static int checkSum;
	private static Boolean result = true;

	// used for writing back to router
	private static PrintWriter out;

	public static void MarketEngineSetUp(String uid, PrintWriter writer) {
		marketUID = uid;
		out = writer;
		try {
            MarketInit.setUpMarket();
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        MarketInit.displayInstrument();
	}

	public static void marketDecisions(String text) {
		// do checksum first

		// used for its main for testing
		//marketUID = "223344";
		//System.out.println(marketUID);
		if (text.contains("list")) {
			textFilter2(text);
			String allInst = "";
			for (int jj = 0; jj < MarketInit.instruments.size(); jj++) {

				// searches through the list of instrument to find the broker searching
				// instrument
				allInst += "=";
				allInst += MarketInit.instruments.get(jj).getName();
				allInst += "-";
				allInst += MarketInit.instruments.get(jj).getPrice();
				allInst += "-";
				allInst += MarketInit.instruments.get(jj).getQuantity();
			}
			if (allInst.length() > 1){
				MarketUtils.printMessage(allInst.substring(1), reply, out);
			} else {
				MarketUtils.printMessage("none", reply, out);
			}
		} else if (textFilter(text)) {
			int checkSum_temp = genCheckSum(command);
			// System.out.println(checkSum_temp);
			// System.out.println(checkSum);
			if (checkSum == checkSum_temp) {
				// buying or selling
				transMode();
			} else {
				MarketUtils.printMessage("Command was corrupted due to not having same checkSum.", reply, out);
			}
		}
        MarketInit.displayInstrument();
	}

	private static void transMode() {
		if (type.toLowerCase().equals("buy")) {
			buyMode();
		} else if (type.toLowerCase().equals("sell")){
			sellMode();
		} else {
			return;
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
					MarketUtils.printMessage("Your purchase of "+quantity+"x"+instrumentName.toLowerCase()+" was a success. "+MarketInit.instruments.get(i).getQuantity()+"x"+instrumentName.toLowerCase()+" remaining.", reply, out);
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
		
		MarketUtils.printMessage("The product you were looking for does not exist or no longer exists anymore", reply, out);
	}

	private static Boolean validBuy(Instrument item) {
		if (price < item.getPrice()) {
			MarketUtils.printMessage("Your buying price is lower than the orginal selling price: " + item.getPrice(), reply, out);
			result = false;
			return false;
		}
		if (quantity > item.getQuantity()) {
			MarketUtils.printMessage("Your buying quanitity is more that the existing item quantity: " + item.getQuantity(), reply, out);
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
					MarketUtils.printMessage("You sold "+quantity+"x"+instrumentName.toLowerCase()+" to market["+marketUID+"] new quantity: "+MarketInit.instruments.get(i).getQuantity(), reply, out);
					return;
				}

				// implemented due to validSell
				if (result == false) {
					return ;
				}
			}
		}
		
		MarketUtils.printMessage("The product you were looking for does not exist or no longer exist anymore", reply, out);
	}

	private static Boolean validSell(Instrument item) {
		if (price > item.getPrice()) {
			MarketUtils.printMessage("Your selling price is higher than the orginal buying price: " + item.getPrice(), reply, out);
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
			reply = clientUID + " " + marketUID + " ";
			return true;
		}
		MarketUtils.printMessage("Broker Sent Market_UID Is Invalid.", reply, out);
		return false;
	}
	private static Boolean textFilter2(String text) {
		String[] orders = null;

		orders = text.split("\\s+");
		if (marketUID.contains(orders[2])) {
			clientUID = orders[0];
			reply = clientUID + " " + marketUID + " ";
			return true;
		}
		MarketUtils.printMessage("Broker Sent Market_UID Is Invalid.", reply, out);
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
	
	//public static void main(String[] args) throws Exception {
    //    try {
    //        MarketInit.setUpMarket();
    //    } catch(Exception e) {
    //        System.out.println(e.getMessage());
    //        System.exit(0);
    //    }
    //    MarketInit.displayInstrument();
		
	//	String text = "123456 sell Iron 12.2 8 223344";
	//	int checkSum = genCheckSum(text);
	//	String command = text + " " + checkSum;
		
	//	// incoming orders testing purposes need to comment out PrintWriter
	//	marketDecisions(command);
    //    MarketInit.displayInstrument();
	//	// marketDecisions(command);
    //    // MarketInit.displayInstrument();
	//}
}
