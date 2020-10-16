package com.endlesshorizon.router.market;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MarketListener {
	private static final int marketPort = 5001;

	private static ArrayList<MarketThread> markets  = new ArrayList<>();
	private static ExecutorService pool = Executors.newFixedThreadPool(4);
	private ServerSocket marketSocket = null;

	public void initiate() throws IOException {
		this.marketSocket = new ServerSocket(marketPort);
		System.out.println("[MARKET_SERVER] Waiting for client connection...");

	}

	public void tryMarketSocket() throws IOException {
		// Creation of the port 5001
		// assign as null for comparison later to check wether it connected
		Socket marketClient = null;
		try {
			//try to accept any incoming connections if none it will stay null if error it will print error
			marketClient = this.marketSocket.accept();
			// once the client is connected onto brokerSocket port
			System.out.println("[MARKET_SERVER] Connected to client!");
		} catch (IOException e) {
			System.out.println("I/O error: " + e);
		}
		// if a market client has connected create a new market thread for them
		if (marketClient != null){
			// lead them to their own thread of doing its own work (think of it as a assistant for just this one client)
			MarketThread marketClientThread = new MarketThread(marketClient);
			// Add this thread to an array of market threads to keep track
			markets.add(marketClientThread);
			// execute the newly created thread
			pool.execute(marketClientThread);
		} else {
			System.out.println("I/O error: ");
		}
	}

	public void start() throws IOException {
		initiate();
	}
}
