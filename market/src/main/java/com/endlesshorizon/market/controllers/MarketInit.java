package com.endlesshorizon.market.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import com.endlesshorizon.market.models.Instrument;

public class MarketInit {

    public static ArrayList<Instrument> instruments = new ArrayList<Instrument>();

    public static void main(String[] args) throws Exception {
        setUpMarket();
        for(int i = 0; i < instruments.size(); i++) {
            System.out.println(instruments.get(i).toString());
        }
    }

    public static void setUpMarket() throws Exception {
		// open a file
		FileReader fileReader;
        try {
            fileReader = new FileReader("./market/src/main/java/com/endlesshorizon/market/controllers/market.txt");
        } catch(Exception e) {
            throw new Exception("error opening market");
		}
		
		BufferedReader bufferReader = new BufferedReader(fileReader);
		String line = bufferReader.readLine();
		
		//		loop (while file has stuff to read) {
		while (line != null) {
		// 		test the line if (line is correcly formated) {
			if (!"".equals(line)) {
				// read file line by line, split it up (by ,) \\ nevermind
				// create new instument(String instrument, float price, int quantity) from each line
				float price = 1;
				int quantity = 5;
				Instrument instrument = new Instrument(line.trim(), price, quantity);
				// add new instument to map
				instruments.add(instrument);
				//		} if end
			}
			line = bufferReader.readLine();
		//		} loop end
		}
		fileReader.close();
	}
}
