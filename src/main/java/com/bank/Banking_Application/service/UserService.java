package com.bank.Banking_Application.service;

import com.bank.Banking_Application.dto.BankResponse;
import com.bank.Banking_Application.dto.CreditDebitRequest;
import com.bank.Banking_Application.dto.EnquiryRequest;
import com.bank.Banking_Application.dto.LoginDto;
import com.bank.Banking_Application.dto.TransferRequest;
import com.bank.Banking_Application.dto.UserRequest;

public interface UserService {
	
	BankResponse createAccount(UserRequest userRequest);
	
	BankResponse balanceEnquiry(EnquiryRequest request);
	
	String nameEnquiry(EnquiryRequest request);
	
	BankResponse creditAccount(CreditDebitRequest request);
	
	BankResponse debitAccount(CreditDebitRequest request);
	
	BankResponse transfer(TransferRequest request);
	
	BankResponse login(LoginDto loginDto);
}
