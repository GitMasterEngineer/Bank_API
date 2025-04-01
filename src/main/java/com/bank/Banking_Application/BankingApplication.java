package com.bank.Banking_Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "The Java Academy Bank App",
		description = "Backend REST API for HDFC Bank", 
		version = "v1.0",
		contact = @Contact(
				name = "Anup Pochchhi",
				email = "anuppochchhi25@gmail.com", 
				url = "https://github.com/GitMasterEngineer/Bank_API"

				), 
		license = @License(
		name = "Tha Java Academy",
		url = "https://github.com/GitMasterEngineer/Bank_API"
		)
		),
			externalDocs = @ExternalDocumentation(
		description = "The Java Academy Bank for Documentation",
		url = "https://github.com/GitMasterEngineer/Bank_API"
	)

)
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

}
