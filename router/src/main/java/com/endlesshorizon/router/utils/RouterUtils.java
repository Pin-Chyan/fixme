package com.endlesshorizon.router.utils;

import java.util.Random;

public class RouterUtils {
	public static String generateID() {
		String ID;

		int leftLimit = 48; // letter '0'
		int rightLimit = 57; // letter '9'
		int targetStringLength = 6;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) 
			  (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		ID = buffer.toString();
		//System.out.println(ID);
		return ID;
	}
}
