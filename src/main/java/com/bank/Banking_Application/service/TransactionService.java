package com.bank.Banking_Application.service;

import com.bank.Banking_Application.dto.TransactionDto;

public interface TransactionService {
	
	void saveTransaction(TransactionDto transactionDto);
}
