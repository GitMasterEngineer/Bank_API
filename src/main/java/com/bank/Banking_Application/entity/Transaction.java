package com.bank.Banking_Application.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction {

		@Id
		@GeneratedValue(strategy = GenerationType.UUID)
		private String transactionId;
		
		private String transactionType;
		
		private BigDecimal amount;
		
		private String accountNumber;
		
		private String status;
}
