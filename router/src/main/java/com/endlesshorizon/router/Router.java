package com.endlesshorizon.router;

import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;

import com.endlesshorizon.router.broker.*;
import com.endlesshorizon.router.market.*;

public class Router {

	public static void main(String[] arg) throws IOException, InterruptedException {
		System.out.println("[Server is online].");

		BrokerListener broker = new BrokerListener();
		MarketListener market = new MarketListener();
		broker.start();
		market.start();
		Thread brokerThread = new Thread() {
			public void run() {
				try {
					broker.tryBrokerSocket();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		Thread marketThread = new Thread() {
			public void run() {
				try {
					market.tryMarketSocket();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		while (true) {
			Thread.sleep(5000);
			
			brokerThread.start();
			marketThread.start();
			brokerThread.join();
			marketThread.join();
		}

	}
}