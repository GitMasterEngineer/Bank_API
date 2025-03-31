package com.bank.Banking_Application.dto;


import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

	private String firstName;
	private String lastName;
	private String otherName;
	private String gender;
	private String address;
	private String stateofOrigin;
	//private String accountNumber;
	//private BigDecimal accountBalance;
	private String email;
	private String phoneNumber;
	private String alternativePhoneNumber;
	
}
