package com.bank.Banking_Application.service;

import com.bank.Banking_Application.dto.BankResponse;
import com.bank.Banking_Application.dto.UserRequest;

public interface UserService {
	
	BankResponse createAccount(UserRequest userRequest);
}
