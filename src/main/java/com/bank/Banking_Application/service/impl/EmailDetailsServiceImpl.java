package com.bank.Banking_Application.service.impl;

import java.io.File;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bank.Banking_Application.dto.EmailDetails;
import com.bank.Banking_Application.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

	@Override
	public void sendEmailWithAttachments(EmailDetails emailDetails) {
		MimeMessage mineMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
			mimeMessageHelper =new MimeMessageHelper(mineMessage,true);
			mimeMessageHelper.setFrom(senderEmail);
			mimeMessageHelper.setTo(emailDetails.getRecipient());
			mimeMessageHelper.setText(emailDetails.getMessageBody());
			mimeMessageHelper.setSubject(emailDetails.getSubject());
			
			FileSystemResource file = new FileSystemResource(new File(emailDetails.getAttachment()));
			mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()),file);
			javaMailSender.send(mineMessage);
			
			log.info(file.getFilename()+" has been send to user with email"+emailDetails.getRecipient());
			
		}catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
	}

}
