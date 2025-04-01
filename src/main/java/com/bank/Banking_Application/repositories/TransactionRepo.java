package com.bank.Banking_Application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.Banking_Application.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, String>{

}
