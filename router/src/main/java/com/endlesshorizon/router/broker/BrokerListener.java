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

	public void run() {
		try {
			ServerSocket brokerSocket = new ServerSocket(brokerPort);

			while (true) {
				System.out.println("[SERVER] Waiting for client connection...");
				Socket brokerClient = brokerSocket.accept();
				System.out.println("[SERVER] Connected to client!");
				BrokerThread brokerClientThread = new BrokerThread(brokerClient);
				brokers.add(brokerClientThread);
				pool.execute(brokerClientThread);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start() {
		run();
	}

}