package com.example.auth.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.Cookie;

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

	public Cookie registerUser(UserEntity user) {
		String encryptedPass = passwordEncoder.encode(user.getPassword());
		user.setPassword(encryptedPass);
		user = repository.save(user);
		String token = jwtHelper.generateToken(user.getEmail());
		return createJWTCookie(token);
	}

	public Cookie loginUser(UserEntity user) {
		Optional<UserEntity> opt = repository.findByEmail(user.getEmail());
		if (opt.isEmpty())
			throw new EntityNotFoundException("User not found or invalid credentials.");
		if (!passwordEncoder.matches(user.getPassword(), opt.get().getPassword()))
			throw new EntityNotFoundException("User not found or invalid credentials.");
		String token = jwtHelper.generateToken(user.getEmail());
		return createJWTCookie(token);
	}

	// TODO: make this configurable
	private Cookie createJWTCookie(String jwtToken) {
		Cookie cookie = new Cookie("jwt", jwtToken);
		cookie.setHttpOnly(true);
		cookie.setPath("/users");
		return cookie;
	}

}
