package com.example.auth.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserEntity body, HttpServletResponse res) {
		Cookie jwtcookie = authService.registerUser(body);
		res.addCookie(jwtcookie);
		Map<String, String> resBody = new HashMap<>();
		resBody.put("msg", "Register success");
		return new ResponseEntity<>(resBody, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserEntity body, HttpServletResponse res) {
		Cookie jwtcookie = authService.loginUser(body);
		res.addCookie(jwtcookie);
		Map<String, String> resBody = new HashMap<>();
		resBody.put("msg", "Login success");
		return new ResponseEntity<>(resBody, HttpStatus.OK);

	}

}
