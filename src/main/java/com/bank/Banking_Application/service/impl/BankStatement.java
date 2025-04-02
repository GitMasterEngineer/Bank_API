package com.bank.Banking_Application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.Banking_Application.dto.EmailDetails;
import com.bank.Banking_Application.entity.Transaction;
import com.bank.Banking_Application.entity.User;
import com.bank.Banking_Application.repositories.TransactionRepo;
import com.bank.Banking_Application.repositories.UserRepo;
import com.bank.Banking_Application.service.EmailService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64.OutputStream;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class BankStatement {

	@Autowired
	private TransactionRepo transactionRepo;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserRepo userRepo;

	private static final String FILE = "C:\\CompanyProject\\Bank\\Banking_Application\\Banking_Application\\MyDocument.pdf";

	// retrieve list of transactions within date range given an account number
	// generate a pdf of trasactions
	// send the file via email

	public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate)
			throws DocumentException, FileNotFoundException {
		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
		System.out.println("Transaction");

		List<Transaction> transactionList = transactionRepo.findAll().stream()
				.filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
				.filter(transaction -> transaction.getCreatedAt() != null)
				.filter(transaction -> transaction.getCreatedAt().isEqual(start))
				.filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();

		User user = userRepo.findByAccountNumber(accountNumber);
		String customerName = user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();
		Rectangle statementSize = new Rectangle(PageSize.A4);
		Document document = new Document(statementSize);
		log.info("Setting size of documents:");
		// OutputStream .io use if any error occur
		FileOutputStream outputStream = new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outputStream);
		document.open();

		PdfPTable bankInfoTable = new PdfPTable(1);
		PdfPCell bankName = new PdfPCell(new Phrase("The Java Academy Bank"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.BLUE);
		bankName.setPadding(20f);

		PdfPCell bankAddress = new PdfPCell(new Phrase("72, Gadge Nagar, Maharashtra, India"));
		bankAddress.setBorder(0);
		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAddress);

		PdfPTable statementInfo = new PdfPTable(2);
		PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date:" + startDate));
		customerInfo.setBorder(0);
		PdfPCell statement = new PdfPCell(new Phrase("STATEMENT ACCOUNT"));
		statement.setBorder(0);
		PdfPCell stopDate = new PdfPCell(new Phrase("End Date:" + endDate));
		stopDate.setBorder(0);

		PdfPCell name = new PdfPCell(new Phrase("Customer Name:" + customerName));
		name.setBorder(0);
		PdfPCell space = new PdfPCell();
		PdfPCell address = new PdfPCell(new Phrase("Customer Address:" + user.getAddress()));
		address.setBorder(0);

		PdfPTable transactionTable = new PdfPTable(4);
		PdfPCell date = new PdfPCell(new Phrase("DATE"));
		date.setBackgroundColor(BaseColor.BLUE);
		date.setBorder(0);

		PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
		transactionType.setBackgroundColor(BaseColor.BLUE);
		transactionType.setBorder(0);
		PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
		transactionAmount.setBackgroundColor(BaseColor.BLUE);
		transactionAmount.setBorder(0);
		PdfPCell status = new PdfPCell(new Phrase("STATUS"));
		status.setBorder(0);

		transactionTable.addCell(date);
		transactionTable.addCell(transactionType);
		transactionTable.addCell(transactionAmount);
		transactionTable.addCell(status);

		transactionList.forEach(transaction -> {
			transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
			transactionTable.addCell(new Phrase(transaction.getTransactionType()));
			transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
			transactionTable.addCell(new Phrase(transaction.getStatus()));
		});

		statementInfo.addCell(customerInfo);
		statementInfo.addCell(statement);
		statementInfo.addCell(endDate);
		statementInfo.addCell(name);
		statementInfo.addCell(space);
		statementInfo.addCell(address);

		document.add(bankInfoTable);
		document.add(statementInfo);
		document.add(transactionTable);

		document.close();

		EmailDetails emailDetails = EmailDetails.builder().recipient(user.getEmail()).subject("STATEMENT OF ACCOUNT")
				.messageBody("Kindly find your requested account statement attached !!").attachment(FILE).build();

		emailService.sendEmailWithAttachments(emailDetails);

		return transactionList;

	}

}
