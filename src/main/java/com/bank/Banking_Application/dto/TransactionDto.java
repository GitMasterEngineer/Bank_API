package com.bank.Banking_Application.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
	
	private String transactionType;
	
	private String accountNumber;
	
	private BigDecimal amount;
	
	private String status;
}
