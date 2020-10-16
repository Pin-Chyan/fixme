package com.endlesshorizon.router.broker;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import com.endlesshorizon.router.utils.RouterUtils;

public class BrokerThread implements Runnable {
	private Socket brokerClient;
	private BufferedReader in;
	private PrintWriter out;

	public BrokerThread(Socket clientSocket) throws IOException {
		this.brokerClient = clientSocket;
		in = new BufferedReader(new InputStreamReader(brokerClient.getInputStream()));
		out = new PrintWriter(brokerClient.getOutputStream(), true);
	}

	@Override
	public void run() {
		try {
			String UID = RouterUtils.generateID();
			// gives UID of the broker
			System.out.println("[BROKER_CLIENT] joined: " + UID);
			out.println("[SERVER] This is your UID: " + UID);
			while (true) {
				String request = in.readLine();

				out.println("[SERVER] recieved this message: " + request);
				System.out.println("[BROKER_CLIENT_UID:" + UID + "] message: " + request);
			}
		} catch (IOException e) {
			System.err.println("IO exception in BrokerThread");
			System.err.println(e.getStackTrace());
		} finally {
			out.close();
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}