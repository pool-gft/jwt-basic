package com.example.auth.api.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.jpa.UserEntity;
import com.example.auth.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@GetMapping("/hi")
	public String hi() {
		return "Helo";
	}

	@GetMapping("/info")
	public UserEntity getUserInfo() {
		String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return service.findUserByEmail(email);
	}
}
