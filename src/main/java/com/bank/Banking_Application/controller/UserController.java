package com.bank.Banking_Application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.Banking_Application.dto.BankResponse;
import com.bank.Banking_Application.dto.CreditDebitRequest;
import com.bank.Banking_Application.dto.EnquiryRequest;
import com.bank.Banking_Application.dto.TransferRequest;
import com.bank.Banking_Application.dto.UserRequest;
import com.bank.Banking_Application.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

		@Autowired
		private UserService userService;
		
		@PostMapping("/")
		public BankResponse createAccount(@RequestBody UserRequest userRequest)
		{
			return this.userService.createAccount(userRequest);
		}
		
		@GetMapping("/balanceEnquiry")
		public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
			return userService.balanceEnquiry(request);
		}
		
		@GetMapping("/nameEnquiry")
		public String nameEnquiry(@RequestBody EnquiryRequest request) {
			return userService.nameEnquiry(request);
		}
		
		@PostMapping("/credit")
		public BankResponse creditAccount(@RequestBody CreditDebitRequest request)
		{
			return userService.creditAccount(request);
		}
		
		@PostMapping("/debit")
		public BankResponse debitAccount(@RequestBody CreditDebitRequest request)
		{
			return userService.debitAccount(request);
		}
		
		@PostMapping("/transfer")
		public BankResponse transferAccount(@RequestBody TransferRequest request)
		{
			return userService.transferAccount(request);
		}
		
		
}
