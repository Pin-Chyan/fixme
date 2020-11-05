package com.endlesshorizon.router.valids;

public class FixMessage {
	private static String broker_uid;
	private static String type;
	private static String instrument;
	private static float price;
	private static int amount;
	private static String market_uid;
	private static int checksum;

	private static String command;
	
	public FixMessage(String message) {
		String[] orders = null;

		System.out.println(message);
		orders = message.split("\\s+");
		broker_uid = orders[0];
		System.out.println(orders[0]);
		type = orders[1];
		System.out.println(orders[1]);
		instrument = orders[2];
		System.out.println(orders[2]);
		price = Float.parseFloat(orders[3]);
		System.out.println(orders[3]);
		amount = Integer.parseInt(orders[4]);
		System.out.println(orders[4]);
		market_uid = orders[5];
		System.out.println(orders[5]);
		checksum = Integer.parseInt(orders[6]);
		System.out.println(orders[6]);
		command = orders[0] + " " + orders[1] + " " + orders[2] + " " + orders[3] + " " + orders[4] + " " + orders[5];
		System.out.println(command);
	}

	public String getCommand() {
		return command;
	}

	public int getChecksum() {
		return checksum;
	}

	public String getMarketUID() {
		return market_uid;
	}
}
