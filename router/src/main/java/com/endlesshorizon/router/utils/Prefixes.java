package com.endlesshorizon.router.utils;

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

	// Servers success
	public static final String FM_S = ANSI_WHITE + "[" + ANSI_GREEN + "SERVER ONLINE" + ANSI_WHITE + "] ";
	public static final String FM_BS = ANSI_WHITE + "[" + ANSI_BLUE + "BROKER_SERVER" + ANSI_WHITE + "] ";
	public static final String FM_MS = ANSI_WHITE + "[" + ANSI_CYAN + "MARKET_SERVER" + ANSI_WHITE + "] ";

	// Server fails
	public static final String FM_S_Error = ANSI_RED + "[" + ANSI_GREEN + "SERVER ONLINE" + ANSI_RED + "] " + ANSI_WHITE;
	public static final String FM_BS_Error = ANSI_RED + "[" + ANSI_BLUE + "BROKER_SERVER" + ANSI_RED + "] " + ANSI_WHITE;
	public static final String FM_MS_Error = ANSI_RED + "[" + ANSI_CYAN + "MARKET_SERVER" + ANSI_RED + "] " + ANSI_WHITE;
	
	// Clients UID's for server
	public static final String FM_BCS = ANSI_WHITE + "[" + ANSI_BLUE + "BROKER_CLIENT_UID:";
	public static final String FM_MCS = ANSI_WHITE + "[" + ANSI_CYAN + "MARKET_CLIENT_UID:";
	
	// Client response
	public static final String FM_BC = ANSI_WHITE + "[" + ANSI_BLUE + "BROKER_CLIENT" + ANSI_WHITE + "] ";
	public static final String FM_MC = ANSI_WHITE + "[" + ANSI_CYAN + "MARKET_CLIENT" + ANSI_WHITE + "] ";

}