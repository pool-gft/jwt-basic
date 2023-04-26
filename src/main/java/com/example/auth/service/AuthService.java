package com.example.auth.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth.jpa.UserEntity;
import com.example.auth.jpa.UserRepo;
import com.example.auth.security.jwt.JWTHelper;

@Service
public class AuthService {

	private UserRepo repository;
	private PasswordEncoder passwordEncoder;
	private JWTHelper jwtHelper;

	public AuthService(UserRepo repository, PasswordEncoder passwordEncoder, JWTHelper jwtHelper) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtHelper = jwtHelper;
	}

	public Map<String, Object> registerUser(UserEntity user) {
		String encryptedPass = passwordEncoder.encode(user.getPassword());
		System.out.println(encryptedPass);
		user.setPassword(encryptedPass);
		user = repository.save(user);
		String token = jwtHelper.generateToken(user.getEmail());
		return Collections.singletonMap("jwt-token", token);
	}

	public Map<String, Object> loginUser(UserEntity user) {
		Optional<UserEntity> opt = repository.findByEmail(user.getEmail());
		if (opt.isEmpty())
			throw new EntityNotFoundException("User not found or invalid credentials.");
		if (!passwordEncoder.matches(user.getPassword(), opt.get().getPassword()))
			throw new EntityNotFoundException("User not found or invalid credentials.");
		String token = jwtHelper.generateToken(user.getEmail());
		return Collections.singletonMap("jwt-token", token);
	}

}
