package com.bank.Banking_Application.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Build;
import org.springframework.stereotype.Service;

import com.bank.Banking_Application.dto.AccountInfo;
import com.bank.Banking_Application.dto.BankResponse;
import com.bank.Banking_Application.dto.CreditDebitRequest;
import com.bank.Banking_Application.dto.EmailDetails;
import com.bank.Banking_Application.dto.EnquiryRequest;
import com.bank.Banking_Application.dto.TransactionDto;
import com.bank.Banking_Application.dto.TransferRequest;
import com.bank.Banking_Application.dto.UserRequest;
import com.bank.Banking_Application.entity.Transaction;
import com.bank.Banking_Application.entity.User;
import com.bank.Banking_Application.repositories.UserRepo;
import com.bank.Banking_Application.service.EmailService;
import com.bank.Banking_Application.service.TransactionService;
import com.bank.Banking_Application.service.UserService;
import com.bank.Banking_Application.utils.Accountutils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private TransactionService transactionService;

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
		User foundUser = userRepo.findByAccountNumber(request.getAccountNumber());
		return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest request) {
		// checking if account exists
		boolean isAccountExists = userRepo.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder().responseCode(Accountutils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(Accountutils.ACCOUNT_NOT_EXISTS_MESSAGE).accountInfo(null).build();
		}
		User userToCredit = userRepo.findByAccountNumber(request.getAccountNumber());
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
		userRepo.save(userToCredit);
		
		//save Transaction
		TransactionDto transactionDto=TransactionDto.builder()
				.accountNumber(userToCredit.getAccountNumber())
				.transactionType("CREDIT")
				.amount(request.getAmount())
				.build();
		
	 transactionService.saveTransaction(transactionDto);

		return BankResponse.builder().responseCode(Accountutils.ACCOUNT_CREDIT_SUCCESS)
				.responseMessage(Accountutils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " "
								+ userToCredit.getOtherName())
						.accountBalance(userToCredit.getAccountBalance()).accountNumber(request.getAccountNumber())
						.build())
				.build();
	}

	// Check if account exists
	// check if account you intend to withdraw is not more than the current Account
	// Balance
	@Override
	public BankResponse debitAccount(CreditDebitRequest request) {
		boolean isAccountExists = userRepo.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder().responseCode(Accountutils.ACCOUNT_NOT_EXISTS_CODE)
					.responseCode(Accountutils.ACCOUNT_NOT_EXISTS_MESSAGE).accountInfo(null).build();
		}

		User userToDebit = userRepo.findByAccountNumber(request.getAccountNumber());
//		int availableBalance = Integer.parseInt(userToDebit.getAccountBalance().toString());
//		int debitAmount = Integer.parseInt(request.getAmount().toString());

		 BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
		 BigInteger debitAmount = request.getAmount().toBigInteger();

		//BigDecimal availableBalance = userToDebit.getAccountBalance();
		//BigDecimal debitAmount = request.getAmount();

		if (availableBalance.intValue() < debitAmount.intValue())
		//if (availableBalance.compareTo(debitAmount) > 0)
			{
			return BankResponse.builder().responseCode(Accountutils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(Accountutils.INSUFFICIENT_BALANCE_MESSAGE).accountInfo(null).build();
		} 
		else {
			userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
			userRepo.save(userToDebit);
			TransactionDto transactionDto=TransactionDto.builder()
					.accountNumber(userToDebit.getAccountNumber())
					.transactionType("CREDIT")
					.amount(request.getAmount())
					.build();
			
		 transactionService.saveTransaction(transactionDto);
			
			return BankResponse.builder().responseCode(Accountutils.ACCOUNT_DEBITED_SUCCESS)
					.responseMessage(Accountutils.ACCOUNT_DEBITED_MESSAGE)
					.accountInfo(AccountInfo.builder().accountNumber(request.getAccountNumber())
							.accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " "
									+ userToDebit.getOtherName())
							.accountBalance(userToDebit.getAccountBalance()).build())
					.build();
		}
	}

	// get the account to debit(check if it exists)
	// check if amount I'm debating is not more than one current balance
	// debit the account
	// get the account to credit
	// credit the account
	@Override
	public BankResponse transfer(TransferRequest request) {
		//boolean isSourceAccountExists=userRepo.existsByAccountNumber(request.getSourceAccountNumber());
		boolean isDestinationAccountExists = userRepo.existsByAccountNumber(request.getDestinationAccountNumber());
		if (!isDestinationAccountExists) {
			return BankResponse.builder()
					.responseCode(Accountutils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(Accountutils.ACCOUNT_NOT_EXISTS_MESSAGE)
					.accountInfo(null).build();
		}
		User sourceAccountUser = userRepo.findByAccountNumber(request.getSourceAccountNumber());
		
		if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
			//System.out.println("INSIFFICIENT BALANCE CHECK");
			return BankResponse.builder()
					.responseCode(Accountutils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(Accountutils.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(null)
					.build();
		}

		sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
		String sourceUsername = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName() + " "
				+ sourceAccountUser.getOtherName();

		userRepo.save(sourceAccountUser);
		
		EmailDetails debitAlert = EmailDetails.builder()
				.subject("DEBIT_ALERT")
				.recipient(sourceAccountUser.getEmail())
				.messageBody("The sum of:" + request.getAmount() + "has been deducted from your current balance id"
						+ sourceAccountUser.getAccountBalance())
				.build();

		emailService.sendEmailAlert(debitAlert);

		User destinationAccountUser = userRepo.findByAccountNumber(request.getDestinationAccountNumber());
		destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
		// String recipientUserName = destinationAccountUser.getFirstName() + " " +
		// destinationAccountUser.getLastName()
		// + " " + destinationAccountUser.getOtherName();
		userRepo.save(destinationAccountUser);
		
		EmailDetails creditAlert = EmailDetails.builder()
				.subject("CREDIT_ALERT")
				.recipient(sourceAccountUser.getEmail())
				.messageBody("The sum of:" + request.getAmount() + "has been sent to your account from" + sourceUsername
						+ "Your Current Balance is" + sourceAccountUser.getAccountBalance())
				.build();

		emailService.sendEmailAlert(creditAlert);
		TransactionDto transactionDto=TransactionDto.builder()
				.accountNumber(destinationAccountUser.getAccountNumber())
				.transactionType("CREDIT")
				.amount(request.getAmount())
				.build();
		
	 transactionService.saveTransaction(transactionDto);

		return BankResponse.builder().responseCode(Accountutils.TRANSFER_SUCCESSFUL_CODE)
				.responseMessage(Accountutils.TRANSFER_SUCCESSFUL_MESSAGE).accountInfo(null).build();
	}
}
