package com.endlesshorizon.router.market;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import com.endlesshorizon.router.utils.Prefixes;
import com.endlesshorizon.router.utils.RouterUtils;

public class MarketThread implements Runnable{
	private Socket marketClient;
	private BufferedReader in;
	private PrintWriter out;

	// interacts with the Socket connected user in its own thread/runnable to do his work alone
	public MarketThread(Socket clientSocket) throws IOException {
		this.marketClient = clientSocket;
		
		// connect to the sockets input stream (this is where the server will read what the client types)
		this.in = new BufferedReader(new InputStreamReader(this.marketClient.getInputStream()));
		
		
		// connect to the sockets output stream (this is where the server will write stuff to the client)
		this.out = new PrintWriter(this.marketClient.getOutputStream(), true);
	}

	@Override
	public void run() {
		try {
			// generate the UID for the broker
			String UID = RouterUtils.generateID();
			System.out.println(Prefixes.FM_MC + "joined: " + UID);
			
			this.out.println(Prefixes.FM_MS + "This is your UID: " + UID);
			this.out.flush();
			while (true) {
				// wait for an input from the market client
				String request = in.readLine();

				//once an input is received print it out to the client aswell as to the server console
				this.out.println(Prefixes.FM_MS + "recieved this message: " + request);
				System.out.println(Prefixes.FM_MCS + UID + Prefixes.ANSI_WHITE + "] message: " + request);
			}
		} catch (IOException e) {
			System.err.println(Prefixes.FM_MS_Error + "IO exception in MarketThread");
			System.out.print(Prefixes.FM_MS_Error + e);
			System.err.println(Prefixes.FM_MS_Error + e.getStackTrace());
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
