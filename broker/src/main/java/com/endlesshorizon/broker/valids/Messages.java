package com.endlesshorizon.broker.valids;

import java.util.Arrays;

public class Messages {
	public static Boolean validFormat(String Command) {
		String[] orders = null;

		orders = Command.split("\\s+");
        System.out.println(Arrays.toString(orders));
		System.out.println(orders.length);
		if (!(orders[0].equals("buy") || orders[0].equals("sell"))) {
			System.out.println("you can only buy or sell");
			return false;
		}
		if (!(isAlphabet(orders[1]))) { // needs to check in market if such a instrument exists
			System.out.println("contains invalid character or numbers");
			return false;
		}
		if (!(isFloat(orders[2]))) {
			System.out.println("is not float");
			return false;
		}
		if (!(isNumber(orders[3]))) {
			System.out.println("is not a number");
			return false;
		}
		if (orders.length != 4) {
			System.out.println("can only be 4 commands");
			return false;
		}
		return true;
	}

	private static Boolean isAlphabet(String word) {
		return word.matches("[a-zA-Z]+"); // another method
		//char[] chars = word.toCharArray();

		//for (char c : chars) {
		//	if(!Character.isLetter(c)) {
		//		return false;
		//	}
		//}
		//return true;
	}

	private static Boolean isFloat(String value) {
		return value.matches("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)");
	}

	private static Boolean isNumber(String value) {
		return value.matches("^[0-9]+$");
	}


	public static void main(String[] args) {
		if (validFormat("buy world 12.3 44")) {
			System.out.println("worked");
		}
		//System.out.println("derp");
	}
}
