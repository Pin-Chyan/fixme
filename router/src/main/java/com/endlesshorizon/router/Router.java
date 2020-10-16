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
		broker.start();
		market.start();
		while (true) {
			// once per second it creates 2 threads that will run simultaneously to check wether there are new connection requests to the respective listeners
			Thread.sleep(1000);

			// creates the threads
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

			//runs the newly created threads
			brokerThread.start();
			marketThread.start();
		}

	}
}