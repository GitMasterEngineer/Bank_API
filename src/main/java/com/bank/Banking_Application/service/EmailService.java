package com.bank.Banking_Application.service;

import com.bank.Banking_Application.dto.EmailDetails;

public interface EmailService {
		
	void sendEmailAlert(EmailDetails emailDetails);
	void sendEmailWithAttachments(EmailDetails emailDetails);
}
