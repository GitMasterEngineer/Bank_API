package com.bank.Banking_Application.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo {
	
	@Schema(
		name = "User account Name"	
		)
	private String accountName;
	
	//once check return type, we can need chenge ot in Bigdecimal
	@Schema(
		name = "User Account Balance"
	)
	private BigDecimal accountBalance;
	
	@Schema(
		name = "User Account Number"
		)
	private String accountNumber;
}
