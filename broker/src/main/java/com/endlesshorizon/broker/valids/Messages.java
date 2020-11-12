package com.endlesshorizon.broker.valids;

//import java.util.Arrays;

import com.endlesshorizon.broker.utils.Prefixes;

public class Messages {
	public static Boolean validFormat(String command) {
		String[] orders = null;

		orders = command.split("\\s+");
        //System.out.println(Arrays.toString(orders));
		//System.out.println(orders.length);
		if (command.toLowerCase().equals("markets")){
			return true;
		}
		if (orders[0].equals("list") && isSixDigit(orders[1]) && isNumber(orders[1])){
			return true;
		}
		if (orders.length != 5) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Make Use Of This Structure: Type Instrument Price Quantity MarketID.");
			return false;
		}
		if (!(orders[0].toLowerCase().equals("buy") || orders[0].toLowerCase().equals("sell"))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Type: Buy Or Sell.");
			return false;
		}
		if (!(isAlphabet(orders[1]))) { // needs to check in market if such a instrument exists
			System.out.println(Prefixes.FM_INVALID_FORM + "Instrument: Contains Invalid Characters.");
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
		if (!(isNumber(orders[4]))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Market_ID: Is Not A Number.");
			return false;
		}
		if (!(isSixDigit(orders[4]))) {
			System.out.println(Prefixes.FM_INVALID_FORM + "Market_ID: Is Not Six Digits.");
			return false;
		}
		return true;
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
	//	String UID = "123456";
	//	String command = "Buy world 12.3 3 223344";
	//	if (validFormat(command)) {
	//		System.out.println(UID + command);
	//		System.out.println(genCheckSum(UID + command));
	//		System.out.println("worked");
	//	}
	//}

	public static int genCheckSum(String message){
        int genCheckSum = 1;
        for (int i = 0; i < message.length(); i++){
            int temp = (int) (Math.floor(Math.log(message.charAt(i)) / Math.log(2))) + 1;
            genCheckSum += ((1 << temp) - 1) ^ message.charAt(i);
        }
        return genCheckSum;
    }
}
