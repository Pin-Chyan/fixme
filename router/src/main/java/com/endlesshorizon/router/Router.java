package com.endlesshorizon.router;

import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;

import com.endlesshorizon.router.utils.*;
import com.endlesshorizon.router.broker.*;
import com.endlesshorizon.router.market.*;

public class Router {

	public static void main(String[] arg) throws IOException, InterruptedException {
		System.out.println(Prefixes.FM_S);

		//create the listeners on each port for the markets and brokers
		BrokerListener broker = new BrokerListener();
		MarketListener market = new MarketListener();
		// start the listeners
		broker.initiate();
		market.initiate();
		while (true) {
			// once per second it creates 2 threads that will run simultaneously to check wether there are new connection requests to the respective listeners
			Thread.sleep(1000);

			// creates the threads
			Thread brokerThread = new Thread();
			createBrokerSocket(broker);
			Thread marketThread = new Thread();
			createMarketSocket(market);

			//runs the newly created threads
			brokerThread.start();
			marketThread.start();
		}
	}

	private static void createBrokerSocket(BrokerListener broker) {
		try {
			broker.tryBrokerSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void createMarketSocket(MarketListener market) {
		try {
			market.tryMarketSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}