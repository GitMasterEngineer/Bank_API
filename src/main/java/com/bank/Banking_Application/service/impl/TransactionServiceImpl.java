package com.bank.Banking_Application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.Banking_Application.dto.TransactionDto;
import com.bank.Banking_Application.entity.Transaction;
import com.bank.Banking_Application.repositories.TransactionRepo;
import com.bank.Banking_Application.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	private TransactionRepo transactionRepo;

	@Override
	public void saveTransaction(TransactionDto transactionDto) {
		Transaction transaction = Transaction.builder()
				.transactionType(transactionDto.getTransactionType())
				.accountNumber(transactionDto.getAccountNumber())
				.status("SUCCESS")
				.amount(transactionDto.getAmount())
				.build();
		transactionRepo.save(transaction);
		System.out.println("Transaction is saved successfull !");
		
	}

}
