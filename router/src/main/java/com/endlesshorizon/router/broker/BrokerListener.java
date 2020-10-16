package com.endlesshorizon.router.broker;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.endlesshorizon.router.utils.*;

public class BrokerListener {
	private static final int brokerPort = 5000;

	private static ArrayList<BrokerThread> brokers  = new ArrayList<>();
	private static ExecutorService pool = Executors.newFixedThreadPool(4);
	private ServerSocket brokerSocket = null;

	public void initiate() throws IOException {
		this.brokerSocket = new ServerSocket(brokerPort);
		System.out.println(Prefixes.FM_BS + "Waiting for client connection...");
	}


	public void tryBrokerSocket() throws IOException {
		// Creation of the port 5000
		// assign as null for comparison later to check wether it connected
		Socket brokerClient = null;
		try {
			//try to accept any incoming connections if none it will stay null if error it will print error
			brokerClient = brokerSocket.accept();
			
			// once the client is connected onto brokerSocket port
			System.out.println(Prefixes.FM_BS + "Connected to client!");
			
		} catch (IOException e) {
			System.out.println(Prefixes.FM_BS_Error + "I/O error: " + e);
		}
		// System.out.print(pool);
		if (brokerClient != null){
			// lead them to their own thread of doing its own work (think of it as a assistant for just this one client)
			BrokerThread brokerClientThread = new BrokerThread(brokerClient);
			// Add this thread to an array of broker threads to keep track
			brokers.add(brokerClientThread);
			// execute the newly created thread
			pool.execute(brokerClientThread);

			// Used to check broker arrays
			//for (int i = 0; i < brokers.size();i++)
			//{ 
			//	System.out.println(Prefixes.FM_BS + "Broker Array: [" + brokers.get(i) + "]");
			//}
		} else {
			System.out.println(Prefixes.FM_BS_Error + "I/O error: ");
		}
	}

	//public void start() throws IOException {
	//	initiate();
	//}

}