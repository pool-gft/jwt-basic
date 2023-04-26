package com.example.auth.api.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.jpa.UserEntity;
import com.example.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	AuthService authService;

	public AuthController(AuthService service) {
		this.authService = service;
	}

	@PostMapping("/register")
	public Map<String, Object> registerUser(@RequestBody UserEntity body) {
		return authService.registerUser(body);
	}

	@PostMapping("/login")
	public Map<String, Object> loginUser(@RequestBody UserEntity body) {
		return authService.loginUser(body);
	}

}
