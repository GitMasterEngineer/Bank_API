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
public class AccountInfo {
		
	private String accountName;
	
	//once check return type, we can need chenge ot in Bigdecimal
	private BigDecimal accountBalance;
	private String accountNumber;
}
