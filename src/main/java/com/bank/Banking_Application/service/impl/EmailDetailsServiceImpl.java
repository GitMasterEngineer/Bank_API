package com.bank.Banking_Application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bank.Banking_Application.dto.EmailDetails;
import com.bank.Banking_Application.service.EmailService;

@Service
public class EmailDetailsServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String senderEmail;

	@Override
	public void sendEmailAlert(EmailDetails emailDetails) {

		try {
			SimpleMailMessage mailmessage = new SimpleMailMessage();
			mailmessage.setFrom(senderEmail);
			mailmessage.setTo(emailDetails.getRecipient());
			mailmessage.setText(emailDetails.getMessageBody());
			mailmessage.setSubject(emailDetails.getSubject());

			javaMailSender.send(mailmessage);
			System.out.println("Mail Send Successfully!!");
		} catch (MailException e) {
			throw new RuntimeException(e);
		}

	}

}
