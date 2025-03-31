package com.bank.Banking_Application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.Banking_Application.entity.User;

public interface UserRepo extends JpaRepository<User, Long>{

		Boolean existsByEmail(String email);
}
