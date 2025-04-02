package com.bank.Banking_Application.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.Banking_Application.entity.Transaction;
import com.bank.Banking_Application.service.impl.BankStatement;
import com.itextpdf.text.DocumentException;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TransactionController {
		
	@Autowired
		private BankStatement bankStatement;
		
		@GetMapping()
		public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
				 @RequestParam String startDate,
				@RequestParam String endDate) throws FileNotFoundException, DocumentException{
		
			return bankStatement.generateStatement(accountNumber, startDate, endDate);
		}
		
}
