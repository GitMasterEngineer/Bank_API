package com.bank.Banking_Application.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.Banking_Application.entity.User;

public interface UserRepo extends JpaRepository<User, Long>{

		Boolean existsByEmail(String email);
		
		Optional<User> findByEmail(String email);
		
		Boolean existsByAccountNumber(String accountNumber);
		
		User findByAccountNumber(String accountNumber);
		
		
}
