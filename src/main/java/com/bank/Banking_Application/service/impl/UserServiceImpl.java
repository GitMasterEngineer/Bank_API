package com.bank.Banking_Application.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Build;
import org.springframework.stereotype.Service;

import com.bank.Banking_Application.dto.AccountInfo;
import com.bank.Banking_Application.dto.BankResponse;
import com.bank.Banking_Application.dto.CreditDebitRequest;
import com.bank.Banking_Application.dto.EmailDetails;
import com.bank.Banking_Application.dto.EnquiryRequest;
import com.bank.Banking_Application.dto.UserRequest;
import com.bank.Banking_Application.entity.User;
import com.bank.Banking_Application.repositories.UserRepo;
import com.bank.Banking_Application.service.EmailService;
import com.bank.Banking_Application.service.UserService;
import com.bank.Banking_Application.utils.Accountutils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private EmailService emailService;

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		// creating an account -> saving a new user into the DB
		// check if the user has already account

		if (userRepo.existsByEmail(userRequest.getEmail())) {
			return BankResponse.builder().responseCode(Accountutils.ACCOUNT_EXISTS_CODE)
					.responseMessage(Accountutils.ACCOUNT_EXISTS_MESSAGE).accountInfo(null).build();

		}

		User newUser = User.builder().firstName(userRequest.getFirstName()).lastName(userRequest.getLastName())
				.otherName(userRequest.getOtherName()).gender(userRequest.getGender()).address(userRequest.getAddress())
				.stateofOrigin(userRequest.getStateofOrigin()).accountNumber(Accountutils.generateAccountNumber())
				.accountBalance(BigDecimal.ZERO).email(userRequest.getEmail()).phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber()).status("ACTIVE").build();

		User savedUser = this.userRepo.save(newUser);

		// send email alerts
		EmailDetails emailDetails = EmailDetails.builder().recipient(savedUser.getEmail()).subject("ACCOUNT_CREATION")
				.messageBody(
						"Congratulations !! Your account has been successfullt created. \n your Account details: \n "
								+ "Account Name:" + savedUser.getFirstName() + " " + savedUser.getLastName() + " "
								+ savedUser.getOtherName() + "\n Account Number:" + savedUser.getAccountNumber())
				.build();
		emailService.sendEmailAlert(emailDetails);

		return BankResponse.builder().responseCode(Accountutils.ACCOUNT_CREATION_SUCCESS)
				.responseMessage(Accountutils.ACCOUNT_CREATION_MWSSAGE)
				.accountInfo(AccountInfo.builder().accountBalance(savedUser.getAccountBalance())
						.accountNumber(savedUser.getAccountNumber()).accountName(savedUser.getFirstName() + " "
								+ savedUser.getLastName() + " " + savedUser.getOtherName())
						.build())
				.build();

	}

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
		// check if the provided account number is exists in the DB

		Boolean isAccountExists = userRepo.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder().responseCode(Accountutils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(Accountutils.ACCOUNT_NOT_EXISTS_MESSAGE).accountInfo(null).build();

		}
		User foundUser = userRepo.findByAccountNumber(request.getAccountNumber());
		return BankResponse.builder().responseCode(Accountutils.ACCOUNT_FOUND_CODE)
				.responseMessage(Accountutils.ACCOUNT_FOUND_SUCCESS)
				.accountInfo(AccountInfo.builder().accountBalance(foundUser.getAccountBalance())
						.accountNumber(foundUser.getAccountNumber()).accountName(foundUser.getFirstName() + " "
								+ foundUser.getLastName() + " " + foundUser.getOtherName())
						.build())
				.build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		Boolean isAccountExists = userRepo.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExists) {
			return Accountutils.ACCOUNT_NOT_EXISTS_MESSAGE;
	}
		User foundUser=userRepo.findByAccountNumber(request.getAccountNumber());
		return foundUser.getFirstName() + " "+foundUser.getLastName() + " "+foundUser.getOtherName();		
  }

	@Override
	public BankResponse creditAccount(CreditDebitRequest request) {
		//checking if account exists
		boolean isAccountExists=userRepo.existsByAccountNumber(request.getAccountNumber());
		if(!isAccountExists) {
			return BankResponse.builder()
					.responseCode(Accountutils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(Accountutils.ACCOUNT_NOT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User userToCredit = userRepo.findByAccountNumber(request.getAccountNumber());
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
		userRepo.save(userToCredit);
		
		return BankResponse.builder()
				.responseCode(Accountutils.ACCOUNT_CREDIT_SUCCESS)
				.responseMessage(Accountutils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName()+ " "+userToCredit.getOtherName())
						.accountBalance(userToCredit.getAccountBalance())
						.accountNumber(request.getAccountNumber())
						.build())
				.build();
	}
}
