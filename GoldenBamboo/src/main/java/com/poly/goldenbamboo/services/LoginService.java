package com.poly.goldenbamboo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.goldenbamboo.entities.AccountEntity;
import com.poly.goldenbamboo.repositories.AccountRepository;

@Service
public class LoginService {
	@Autowired
	private AccountRepository accountJPA;

	public AccountEntity login(String phone, String password) {
		AccountEntity user = accountJPA.findByPhone(phone);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}
}
