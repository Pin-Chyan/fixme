package com.endlesshorizon.router.broker;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import com.endlesshorizon.router.utils.RouterUtils;

public class BrokerThread implements Runnable {
	private Socket brokerClient;
	private BufferedReader in;
	private PrintWriter out;

	// interacts with the Socket connected user in its own thread/runnable to do his work alone
	public BrokerThread(Socket clientSocket) throws IOException {
		this.brokerClient = clientSocket;
		
		//
		this.in = new BufferedReader(new InputStreamReader(this.brokerClient.getInputStream()));
		
		
		//
		this.out = new PrintWriter(this.brokerClient.getOutputStream(), true);
	}

	@Override
	public void run() {
		try {
			// generate the UID for the broker
			String UID = RouterUtils.generateID();
			System.out.println("[BROKER_CLIENT] joined: " + UID);
			
			this.out.println("[BROKER_SERVER] This is your UID: " + UID);
			this.out.flush();
			while (true) {
				String request = in.readLine();

				this.out.println("[BROKER_SERVER] recieved this message: " + request);
				System.out.println("[BROKER_CLIENT_UID:" + UID + "] message: " + request);
			}
		} catch (IOException e) {
			System.err.println("IO exception in BrokerThread");
			System.err.println(e.getStackTrace());
		} finally {
			this.out.close();
			try {
				this.in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}