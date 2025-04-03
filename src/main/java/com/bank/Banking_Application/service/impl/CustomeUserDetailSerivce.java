package com.bank.Banking_Application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bank.Banking_Application.repositories.UserRepo;
@Service
public class CustomeUserDetailSerivce implements UserDetailsService{
	
	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException(username+" Not found"));
	}
	
		
}
