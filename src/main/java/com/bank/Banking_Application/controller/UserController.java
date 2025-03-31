package com.bank.Banking_Application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.Banking_Application.dto.BankResponse;
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
}
