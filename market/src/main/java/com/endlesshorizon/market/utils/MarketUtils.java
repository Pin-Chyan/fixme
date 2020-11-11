package com.endlesshorizon.market.utils;

import java.io.PrintWriter;

public class MarketUtils {
	public static void printMessage(String message, String reply, PrintWriter out) {
		System.out.println(message);
		out.println(reply + message);
		out.flush();
	}
}
