package com.endlesshorizon.router.valids;

public class MarkMessage {
	private static String broker_uid;
	private static String market_uid;

	public MarkMessage(String message) {
		String[] orders = null;

		//System.out.println(message);
		orders = message.split("\\s+");
		broker_uid = orders[0];
		//System.out.println(orders[0]);
		market_uid = orders[1];
		//System.out.println(orders[1]);
	}

	public String getBrokerUID() {
		return broker_uid;
	}
}
