package com.endlesshorizon.market.valids;

import com.endlesshorizon.market.utils.Prefixes;

public class Messages {
	public static Boolean validFormat(String Command) {
		String[] orders = null;

		orders = Command.split("\\s+");
        //System.out.println(Arrays.toString(orders));
		//System.out.println(orders.length);
		if (orders.length != 4) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Make Use Of This Structure: Market Instrument Price Quantity.");
			return false;
		}
		if (!(isAlphabet(orders[1]))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Market Name: Contains Invalid Characters.");
			return false;
		}
		if (!(isAlphabet(orders[1]))) { // needs to check in market if such a instrument exists
			System.out.println(Prefixes.FM_INVALID_FORM + "Instrument Name: Contains Invalid Characters.");
			return false;
		}
		if (!(isFloat(orders[2]))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Price: Is Not A Float.");
			return false;
		}
		if (!(isNumber(orders[3]))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Quantity: Is Not A Number.");
			return false;
		}
		return true;
	}

	private static Boolean isAlphabet(String word) {
		return word.matches("[a-zA-Z]+");
	}

	private static Boolean isFloat(String value) {
		return value.matches("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)");
	}

	private static Boolean isNumber(String value) {
		return value.matches("^[0-9]+$");
	}
}
