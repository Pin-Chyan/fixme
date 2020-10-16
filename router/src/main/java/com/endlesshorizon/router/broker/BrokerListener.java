package com.endlesshorizon.router.broker;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BrokerListener {
	private static final int brokerPort = 5000;

	private static ArrayList<BrokerThread> brokers  = new ArrayList<>();
	private static ExecutorService pool = Executors.newFixedThreadPool(4);
	private ServerSocket brokerSocket = null;

	public void initiate() throws IOException {
		this.brokerSocket = new ServerSocket(brokerPort);
		System.out.println("[BROKER_SERVER] Waiting for client connection...");
	}


	public void tryBrokerSocket() throws IOException {
		// Creation of the port 5000
		try(Socket brokerClient = brokerSocket.accept()) {
			// once the client is connected onto brokerSocket port
			
			System.out.println("[BROKER_SERVER] Connected to client!");
		
			// lead them to their own thread of doing its own work (think of it as a assistant for just this one client)
			BrokerThread brokerClientThread = new BrokerThread(brokerClient);
			brokers.add(brokerClientThread);
			pool.execute(brokerClientThread);

			// Used to check broker arrays
			//for (int i = 0; i < brokers.size();i++)
			//{ 
			//	System.out.println("[SERVER] Broker Array: [" + brokers.get(i) + "]");
			//}
		}
	}

	public void start() throws IOException {
		initiate();
	}

}