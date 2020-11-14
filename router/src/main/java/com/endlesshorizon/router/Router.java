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
import com.endlesshorizon.router.valids.FixMessage;
import com.endlesshorizon.router.valids.MarkMessage;

public class Router {
	// for each connected clients store their ID and their printwriter to write to their displays
	private static Set<Map<String, OutputStream>> brokerWriters = new HashSet<>();
	private static Set<Map<String, PrintWriter>> marketWriters = new HashSet<>();

	private static int Broker_port = 5000;
	private static int Market_port = 5001;

	public static void main(String[] arg) throws IOException, InterruptedException {
		System.out.println(Prefixes.FM_S);

		//create the listeners on each port for the markets and brokers
		Thread brokers = new Thread(new BrokerListener());
		brokers.start();

		Thread market = new Thread(new MarketListener());
		market.start();

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
		private Boolean exist = false;
		
		public BrokerThread(Socket socket) {
			this.socket = socket;
		} 
		
		@Override
		public void run() {
			OutputStream outStream = null;
			try {
				in = new Scanner(socket.getInputStream());
				outStream = socket.getOutputStream();
				out = new PrintWriter(socket.getOutputStream(), true);
				uid = RouterUtils.generateID();
			} catch (IOException e) {
				System.err.println(Prefixes.FM_BS_Error + "IO exception in BrokerThread");
				System.out.print(Prefixes.FM_BS_Error + e);
				System.err.println(Prefixes.FM_BS_Error + e.getStackTrace());
			}
			out.println(Prefixes.FM_BS + "This is your UID: " + uid);
			System.out.println(Prefixes.FM_BC + "joined: " + uid);
			Map<String, OutputStream> broker = new HashMap<String, OutputStream>();
			broker.put(uid, outStream);
			brokerWriters.add(broker);
			while (true) {
				String request = "";
				try {
					request = in.nextLine();
				} catch (NoSuchElementException exec32){
					System.out.println("Broker["+uid+"] Disconnected");
					return;
				}

				// filter out empty requests out
				this.out.println(Prefixes.FM_BS + "recieved this message: " + request);
				this.out.flush();
				if (!(request.isEmpty())) {
					System.out.println("req["+request+"]");
					if (!request.toLowerCase().contains("list") && !request.toLowerCase().contains("markets")){
						FixMessage fix = new FixMessage(request);
						int checksum_temp = genCheckSum(fix.getCommand());
						// after checksum validation send message to desired market
						if (fix.getChecksum() == checksum_temp) {
							//System.out.println("send command/request to market");
							for (Map<String, PrintWriter> m_Writer : marketWriters) {
								//System.out.println("found the Market Map");
								for (String market_identity : m_Writer.keySet()) {
									//System.out.println("going through marketlist to find the specific market");
									System.out.println(market_identity + " | " + fix.getMarketUID());
									if (fix.getMarketUID().contains(market_identity)) {
										exist = true;
										//System.out.println("found the market");
										PrintWriter writer = m_Writer.get(market_identity);
										System.out.println("Sending Request.");
										writer.print(request + "\n");
										writer.flush();
									}
								}
							}
							if (exist == false) {
								out.println(fix.getBrokerUID() + " " + fix.getMarketUID() + " The MarketUID you gave does not exist");
								out.flush();
							}
						}
						//System.out.println(fix.getChecksum() + " || " + checksum_temp);
					} else if (request.toLowerCase().contains("markets")){
						String allmarkets = "";
						for (Map<String, PrintWriter> m_Writer : marketWriters) {
							System.out.println("found the Market Map");
							for (String market_identity : m_Writer.keySet()) {
								System.out.println("getting all the markets");
								allmarkets += "=";
								allmarkets += market_identity;
							}
						}
						if (allmarkets.length() > 1){
							this.out.println(allmarkets.substring(1));
							this.out.flush();
						} else {
							this.out.println("none");
							this.out.flush();
						}
					} else {
						String[] cmdss = null;

						cmdss = request.split("\\s+");
						//System.out.println(request);
						//System.out.println("Getting list from market");
						for (Map<String, PrintWriter> m_Writer : marketWriters) {
							//System.out.println("found the Market Map");
							for (String market_identity : m_Writer.keySet()) {
								//System.out.println("going through marketlist to find the specific market");
								System.out.println(market_identity + " | " + cmdss[2]);
								if (cmdss[2].contains(market_identity)) {
									//System.out.println("found the market");
									PrintWriter writer = m_Writer.get(market_identity);
									System.out.println("Sending Request.");
									writer.println(request);
									writer.flush();
								}
							}
						}
					}
				}
				System.out.println(Prefixes.FM_BCS + uid + Prefixes.ANSI_WHITE + "] message: " + request);
			}
		}
	}

	private static class MarketListener implements Runnable {
		@Override
		public void run() {
			ExecutorService pool = Executors.newFixedThreadPool(4);
			try (ServerSocket listener = new ServerSocket(Market_port)) {
				while (true) {
					pool.execute(new MarketThread(listener.accept()));
				}
			} catch (IOException e) {}
			catch (NoSuchElementException e) {
				System.out.println("Disconnection Detected! on MarketListener");
			}
		}
	}

	private static class MarketThread implements Runnable {
		private String uid;
		private Socket socket;
		private Scanner in;
		private PrintWriter out;

		public MarketThread(Socket socket) throws NullPointerException {
			this.socket = socket;
		}

		@Override public void run() throws NullPointerException {
			try {
				in = new Scanner(socket.getInputStream());
				out = new PrintWriter(socket.getOutputStream(), true);
				uid = RouterUtils.generateID();
			} catch (IOException e) {
				System.err.println(Prefixes.FM_MS_Error + "IO exception in MarketThread");
				System.out.print(Prefixes.FM_MS_Error + e);
				System.err.println(Prefixes.FM_MS_Error + e.getStackTrace());
			}
			out.println(Prefixes.FM_MS + "This is your UID: " + uid);
			System.out.println(Prefixes.FM_MC + "joined: " + uid);
			Map<String, PrintWriter> market = new HashMap<String, PrintWriter>();
			market.put(uid, out);
			marketWriters.add(market);
			while(true) {
				String request = "";
				try {
					request = in.nextLine();
				} catch (NoSuchElementException exep) {
					System.out.println("Market["+uid+"] Disconnected");
					request = "";
					System.out.println("Deleting marketUID");
					for (Map<String, PrintWriter> m_Writer : marketWriters) {
						for (String market_identity : m_Writer.keySet()) {
							if (uid.contains(market_identity)) {
								m_Writer.remove(market_identity);
								System.out.println("Deleted");
							}
						}
					}
					return;
				}

				if (!(request.isEmpty())) {
					MarkMessage mark = new MarkMessage(request);
					for (Map<String, OutputStream> b_Writer : brokerWriters) {
						//System.out.println("found the Broker Map");
						for (String broker_identity : b_Writer.keySet()) {
							//System.out.println("going through marketlist to find specific broker");
							System.out.println(broker_identity + " | " + mark.getBrokerUID());
							if (mark.getBrokerUID().contains(broker_identity)) {
								System.out.println("found broker");
								System.out.println(b_Writer.get(broker_identity));
								PrintWriter writer = new PrintWriter(b_Writer.get(broker_identity), true);
								System.out.println("Sending Request.");
								System.out.println("Request: "+request);
								writer.println(request);
								writer.flush();
							}
						}
					}
				}

			}
		}
	}

	private static int genCheckSum(String message){
        int genCheckSum = 1;
        for (int i = 0; i < message.length(); i++){
            int temp = (int) (Math.floor(Math.log(message.charAt(i)) / Math.log(2))) + 1;
            genCheckSum += ((1 << temp) - 1) ^ message.charAt(i);
        }
        return genCheckSum;
    }
}