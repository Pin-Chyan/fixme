package com.endlesshorizon.broker.utils;

/**
 * prefixes
 */
public class Prefixes {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	// Messages
	public static final String FM_INVALID_FORM = ANSI_WHITE + "[" + ANSI_RED + "INVALID FORMAT" + ANSI_WHITE + "] ";

	// Broker
	public static final String FM_BCON = ANSI_WHITE + "[" + ANSI_PURPLE + "BROKER_CLIENT" + ANSI_WHITE + "] ";
	public static final String FM_BERR = ANSI_RED + "[" + ANSI_PURPLE + "BROKER_CLIENT" + ANSI_RED + "] ";
}