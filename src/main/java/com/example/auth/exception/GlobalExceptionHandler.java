package com.example.auth.exception;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("msg", ex.getMessage());
		body.put("stack", ex.getStackTrace());
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

}
