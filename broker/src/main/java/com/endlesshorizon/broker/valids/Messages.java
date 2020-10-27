package com.endlesshorizon.broker.valids;

import java.util.Arrays;

import com.endlesshorizon.broker.utils.Prefixes;

public class Messages {
	public static Boolean validFormat(String command, String uid) {
		String[] orders = null;

		orders = command.split("\\s+");
        //System.out.println(Arrays.toString(orders));
		//System.out.println(orders.length);
		if (orders.length != 6) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Make Use Of This Structure: Broker_ID Market_ID Type Instrument Price Quantity.");
			return false;
		}
		if (!(isNumber(orders[0]))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Broker_ID: Is Not A Number.");
			return false;
		}
		if (!(isSixDigit(orders[0]))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Broker_ID: Is Not Six Digits.");
			return false;
		}
		if (!(uidMatch(orders[0], uid))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Broker_ID: Is Not Matched With Your Given UID.");
			return false;
		}
		if (!(isNumber(orders[1]))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Market_ID: Is Not A Number.");
			return false;
		}
		if (!(isSixDigit(orders[1]))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Market_ID: Is Not Six Digits.");
			return false;
		}
		if (!(orders[2].toLowerCase().equals("buy") || orders[2].toLowerCase().equals("sell"))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Type: Buy Or Sell.");
			return false;
		}
		if (!(isAlphabet(orders[3]))) { // needs to check in market if such a instrument exists
			System.out.println(Prefixes.FM_INVALID_FORM + "Instrument: Contains Invalid Characters.");
			return false;
		}
		if (!(isFloat(orders[4]))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Price: Is Not A Float.");
			return false;
		}
		if (!(isNumber(orders[5]))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Quantity: Is Not A Number.");
			return false;
		}
		return true;
	}

	private static Boolean uidMatch(String word, String uid) {
		if (word.contains(uid)) {
			//System.out.println(word);
			//System.out.println(uid);
			return true;
		}
		return false;
	}

	private static Boolean isSixDigit(String word) {
		return word.matches("\\d{6}");
	}

	//private static Boolean validCharacters(String word) {
	//	return word.matches("^[a-zA-Z0-9_.-]*$");
	//}

	private static Boolean isAlphabet(String word) {
		return word.matches("[a-zA-Z]+");
	}

	private static Boolean isFloat(String value) {
		return value.matches("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)");
	}

	private static Boolean isNumber(String value) {
		return value.matches("^[0-9]+$");
	}

	////Test Purposes
	//public static void main(String[] args) {
	//	String uid = "422122";
	//	String command = "422122 222221 Buy world 12.3 3";
	//	if (validFormat(command, uid)) {
	//		genCheckSum(command);
	//		System.out.println(genCheckSum(command));
	//		System.out.println("worked");
	//	}
	//	System.out.println("derp");
	//}

	//public static int genCheckSum(String message){
    //    int genCheckSum = 1;
    //    for (int i = 0; i < message.length(); i++){
    //        int temp = (int) (Math.floor(Math.log(message.charAt(i)) / Math.log(2))) + 1;
    //        genCheckSum += ((1 << temp) - 1) ^ message.charAt(i);
    //    }
    //    return genCheckSum;
    //}
}
