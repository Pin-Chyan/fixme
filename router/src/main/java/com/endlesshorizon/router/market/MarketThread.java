package com.endlesshorizon.router.market;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import com.endlesshorizon.router.utils.RouterUtils;

public class MarketThread implements Runnable{
	private Socket marketClient;
	private BufferedReader in;
	private PrintWriter out;

	// interacts with the Socket connected user in its own thread/runnable to do his work alone
	public MarketThread(Socket clientSocket) throws IOException {
		this.marketClient = clientSocket;
		
		//
		in = new BufferedReader(new InputStreamReader(marketClient.getInputStream()));
		
		
		//
		out = new PrintWriter(marketClient.getOutputStream(), true);
	}

	@Override
	public void run() {
		try {
			// generate the UID for the broker
			String UID = RouterUtils.generateID();
			System.out.println("[MARKET_CLIENT] joined: " + UID);
			
			out.println("[MARKET_SERVER] This is your UID: " + UID);
			while (true) {
				String request = in.readLine();

				out.println("[MARKET_SERVER] recieved this message: " + request);
				System.out.println("[MARKET_CLIENT_UID:" + UID + "] message: " + request);
			}
		} catch (IOException e) {
			System.err.println("IO exception in MarketThread");
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
