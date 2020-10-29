package com.endlesshorizon.router;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
//import java.net.ServerSocket;
//import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.endlesshorizon.router.utils.*;
//import com.endlesshorizon.router.broker.*;
//import com.endlesshorizon.router.market.*;

public class Router {
	// for each connected clients store their ID and their printwriter to write to their displays
	private static Set<Map<String, PrintWriter>> brokerWriters = new HashSet<>();
	private static Set<Map<String, PrintWriter>> marketWriters = new HashSet<>();

	private static int Broker_port = 5000;
	private static int Market_port = 5001;

	public static void main(String[] arg) throws IOException, InterruptedException {
		System.out.println(Prefixes.FM_S);

		//create the listeners on each port for the markets and brokers
		Thread brokers = new Thread(new BrokerListener());
		brokers.start();

	}

	private static class BrokerListener implements Runnable {
		@Override
		public void run() {
			ExecutorService pool = Executors.newFixedThreadPool(4);
			try (ServerSocket listener = new ServerSocket(Broker_port)) {
				while (true) {
					pool.execute(new BrokerThread(listener.accept()));
				}
			} catch (IOException e) {}
			catch (NoSuchElementException e) {
				System.out.println("Disconnection Detected! on BrokerListener");
			}
		}
	}

	private static class BrokerThread implements Runnable {
		private String uid;
		private Socket socket;
		private Scanner in;
		private PrintWriter out;
		
		public BrokerThread(Socket socket) {
			this.socket = socket;
		} 
		
		@Override
		public void run() {
			try {
				in = new Scanner(socket.getInputStream());
				out = new PrintWriter(socket.getOutputStream(), true);
				uid = RouterUtils.generateID();
			} catch (IOException e) {
				System.err.println(Prefixes.FM_MS_Error + "IO exception in BrokerThread");
				System.out.print(Prefixes.FM_MS_Error + e);
				System.err.println(Prefixes.FM_MS_Error + e.getStackTrace());
			}
			out.println(Prefixes.FM_MS + "This is your UID: " + uid);
			System.out.println(Prefixes.FM_MC + "joined: " + uid);
			Map<String, PrintWriter> broker = new HashMap<String, PrintWriter>();
			broker.put(uid, out);
			brokerWriters.add(broker);
			while (true) {
				String request = in.nextLine();

				//once an input is received print it out to the client aswell as to the server console
				this.out.println(Prefixes.FM_MS + "recieved this message: " + request);
				System.out.println(Prefixes.FM_MCS + uid + Prefixes.ANSI_WHITE + "] message: " + request);
			}
		}
	}

	private static class MarketListener implements Runnable {
		@Override
		public void run() {
			ExecutorService pool = Executors.newFixedThreadPool(4);
			try (ServerSocket listener = new ServerSocket(Market_port)) {
				while (true) {
					pool.execute(new BrokerThread(listener.accept()));
				}
			} catch (IOException e) {}
			catch (NoSuchElementException e) {
				System.out.println("Disconnection Detected! on MarketListener");
			}
		}
	}
}