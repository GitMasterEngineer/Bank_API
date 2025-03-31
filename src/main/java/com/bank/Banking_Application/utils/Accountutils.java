package com.bank.Banking_Application.utils;

import java.time.Year;

public class Accountutils {
	
	public static final String ACCOUNT_EXISTS_CODE="001";
	
	public static final String ACCOUNT_EXISTS_MESSAGE="This user already has an account created!!";
	
	public static final String ACCOUNT_CREATION_SUCCESS="002";
	
	public static final String ACCOUNT_CREATION_MWSSAGE="Account has been Successfully created!!";

	public static String generateAccountNumber() {

		Year currentYear = Year.now();

		int min = 100000;

		int max = 999999;

		// generate random number between min and max

		int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

		// convert the current and randomNumber to Strings then concatination

		String year = String.valueOf(currentYear);
		String randomNumber = String.valueOf(randNumber);
		StringBuilder accountNumber = new StringBuilder();
		return accountNumber.append(year).append(randomNumber).toString();
	}
}
