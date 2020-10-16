package com.endlesshorizon.router;

import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;

import com.endlesshorizon.router.broker.*;

public class Router {

	public static void main(String[] arg) throws IOException{
		System.out.println("waiting for a connection.");
		BrokerListener broker = new BrokerListener();
		broker.start();

	}
}