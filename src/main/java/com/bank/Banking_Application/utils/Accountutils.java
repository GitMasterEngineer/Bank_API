package com.bank.Banking_Application.utils;

import java.time.Year;

public class Accountutils {

	public static final String ACCOUNT_EXISTS_CODE = "001";

	public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account created!!";

	public static final String ACCOUNT_CREATION_SUCCESS = "002";

	public static final String ACCOUNT_CREATION_MWSSAGE = "Account has been Successfully created!!";

	public static final String ACCOUNT_NOT_EXISTS_CODE = "003";

	public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "User with privided accountNumber does not exists!";

	public static final String ACCOUNT_FOUND_CODE = "004";

	public static final String ACCOUNT_FOUND_SUCCESS = "User Account Found!!";

	public static final String ACCOUNT_CREDIT_SUCCESS = "005";

	public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User Account was Successfully credited!";

	public static final String INSUFFICIENT_BALANCE_CODE = "006";

	public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance !";

	public static final String ACCOUNT_DEBITED_SUCCESS = "007";

	public static final String ACCOUNT_DEBITED_MESSAGE = "Balance has been debited successfully!";
	
	public static final String TRANSFER_SUCCESSFUL_CODE="008";
	
	public static final String TRANSFER_SUCCESSFUL_MESSAGE="Transfer is Successfull!!";

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
