package com.endlesshorizon.router.broker;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import com.endlesshorizon.router.utils.*;


public class BrokerThread implements Runnable {
	private Socket brokerClient;
	private BufferedReader in;
	private PrintWriter out;
	private Boolean checkSum = false;

	// interacts with the Socket connected user in its own thread/runnable to do his work alone
	public BrokerThread(Socket clientSocket) throws IOException {
		this.brokerClient = clientSocket;
		
		// connect to the sockets input stream (this is where the server will read what the client types)
		this.in = new BufferedReader(new InputStreamReader(this.brokerClient.getInputStream()));
		
		
		// connect to the sockets output stream (this is where the server will write stuff to the client)
		this.out = new PrintWriter(this.brokerClient.getOutputStream(), true);
	}

	@Override
	public void run() {
		String UID = null;
		try {
			// generate the UID for the broker
			UID = RouterUtils.generateID();
			BrokerListener.addBrokerUID(UID);
			System.out.println(Prefixes.FM_BC + "joined: " + UID);
			
			this.out.println(Prefixes.FM_BS + "This is your UID: " + UID);
			this.out.flush();
			while (true) {
				if (BrokerListener.verifyBrokerUID(UID)){
					// wait for an input from the broker client
					String request = in.readLine();

					//once an input is received print it out to the client aswell as to the server console
					if (checkSum == false) {
						checkSum = true;
						if (requestLine(request)) {
							this.out.println(Prefixes.FM_BS + "recieved this message: " + request);
						 	System.out.println(Prefixes.FM_BCS + UID + Prefixes.ANSI_WHITE +"] message: " + request);
							 System.out.println("CheckSum is a match");
							} else {
								System.out.println("CheckSum is not a match");
							}
						}
					this.out.println(Prefixes.FM_BS + "recieved this message: " + request);
					System.out.println(Prefixes.FM_BCS + UID + Prefixes.ANSI_WHITE +"] message: " + request);
					
					this.out.println(Prefixes.FM_BS + "recieved this message: " + request);
				} else {
					this.out.println(Prefixes.FM_BS + "you are not authorized");
					System.out.println(Prefixes.FM_BCS + UID + Prefixes.ANSI_WHITE +"] unauthorized connection");
				}
			}
		} catch (IOException e) {
			if (e.toString().contains("Connection reset")){
				System.err.println(Prefixes.FM_BS_Error + e.toString());
				System.err.println(Prefixes.FM_BS_Error + "Broker["+UID+"] has disconnected");
				BrokerListener.removeBrokerUID(UID);
			} else {
				System.err.println(Prefixes.FM_BS_Error + "IO exception in BrokerThread");
				System.out.print(Prefixes.FM_BS_Error + e + "\n");
				System.err.println(Prefixes.FM_BS_Error + e.getStackTrace());
			}
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

	
	public static int genCheckSum(String message){
        int genCheckSum = 1;
        for (int i = 0; i < message.length(); i++){
            int temp = (int) (Math.floor(Math.log(message.charAt(i)) / Math.log(2))) + 1;
            genCheckSum += ((1 << temp) - 1) ^ message.charAt(i);
        }
        return genCheckSum;
	}
	
	private static Boolean requestLine(String req) {
		String[] orders = null;
		String command;
		int checkSum;
		int checkSum_temp;

		orders = req.split("\\s+");
		command = orders[0] + " " + orders[1] + " " + orders[2] + " " + orders[3] + " " + orders[4] + " " + orders[5];
		checkSum = Integer.parseInt(orders[6]);
		checkSum_temp = genCheckSum(command);
		System.out.println(orders[6]);
		System.out.println(checkSum_temp);
		System.out.println("once");
		if (checkSum == checkSum_temp) {
			return true;
		} else {
			System.out.println("Command was corrupted due to none same checkSum.");
			return false;	
		}
	}

}