package com.endlesshorizon.router.market;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MarketListener {
	private static final int brokerPort = 5001;

	private static ArrayList<MarketThread> markets  = new ArrayList<>();
	private static ExecutorService pool = Executors.newFixedThreadPool(4);
	private ServerSocket marketSocket = null;

	public void initiate() throws IOException {
		this.marketSocket = new ServerSocket(brokerPort);
		System.out.println("[MARKET_SERVER] Waiting for client connection...");

	}

	public void tryMarketSocket() throws IOException {
		// Creation of the port 5001


		try(Socket marketClient = marketSocket.accept()) {
			// once the client is connected onto marketSocket port
			System.out.println("[MARKET_SERVER] Connected to client!");
		
			// lead them to their own thread of doing its own work (think of it as a assistant for just this one client)
			MarketThread marketClientThread = new MarketThread(marketClient);
			markets.add(marketClientThread);
			pool.execute(marketClientThread);

			// Used to check broker arrays
			//for (int i = 0; i < markets.size();i++)
			//{ 
			//	System.out.println("[SERVER] Broker Array: [" + markets.get(i) + "]");
			//}
		}

		//while (true) {
		//	// once the client is connected onto marketSocket port
		//	Socket marketClient = marketSocket.accept();
		//	System.out.println("[MARKET_SERVER] Connected to client!");
		
		//	// lead them to their own thread of doing its own work (think of it as a assistant for just this one client)
		//	MarketThread marketClientThread = new MarketThread(marketClient);
		//	markets.add(marketClientThread);
		//	pool.execute(marketClientThread);

		//	// Used to check broker arrays
		//	//for (int i = 0; i < markets.size();i++)
		//	//{ 
		//	//	System.out.println("[SERVER] Broker Array: [" + markets.get(i) + "]");
		//	//}
		//}
	}

	public void start() throws IOException {
		initiate();
	}
}
