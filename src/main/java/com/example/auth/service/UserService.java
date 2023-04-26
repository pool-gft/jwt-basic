package com.example.auth.service;

import java.util.Collections;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.auth.jpa.UserEntity;
import com.example.auth.jpa.UserRepo;

@Service
public class UserService implements UserDetailsService {

	UserRepo repository;

	public UserService(UserRepo repository) {
		this.repository = repository;
	}

	public UserEntity findUserByEmail(String email) {
		Optional<UserEntity> opt = repository.findByEmail(email);
		if (opt.isEmpty())
			throw new EntityNotFoundException("User not found.");
		return opt.get();
	}

	public UserDetails loadUserByUsername(String email) {
		Optional<UserEntity> opt = repository.findByEmail(email);
		if (opt.isEmpty())
			throw new EntityNotFoundException("User not found.");
		return new User(email, opt.get().getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

	}

}
