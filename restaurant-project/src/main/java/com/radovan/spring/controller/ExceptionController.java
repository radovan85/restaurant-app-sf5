package com.radovan.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.radovan.spring.exceptions.ExistingEmailException;
import com.radovan.spring.exceptions.ImagePathException;
import com.radovan.spring.exceptions.InvalidCartException;
import com.radovan.spring.exceptions.InvalidUserException;
import com.radovan.spring.exceptions.SuspendedUserException;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(ExistingEmailException.class)
	public ResponseEntity<?> handleExistingEmailException(ExistingEmailException ex) {
		return ResponseEntity.internalServerError().body("Email exists already!");
	}

	@ExceptionHandler(InvalidUserException.class)
	public ResponseEntity<?> handleInvalidUserException(InvalidUserException ex) {
		return ResponseEntity.internalServerError().body("Invalid user!");
	}

	@ExceptionHandler(InvalidCartException.class)
	public ResponseEntity<?> handleInvalidCartException(InvalidCartException ex) {
		return ResponseEntity.internalServerError().body("Invalid cart");
	}

	@ExceptionHandler(SuspendedUserException.class)
	public ResponseEntity<?> handleSuspendedUserException(SuspendedUserException ex) {
		SecurityContextHolder.clearContext();
		return ResponseEntity.internalServerError().body("Account Suspended!");
	}

	@ExceptionHandler(ImagePathException.class)
	public ResponseEntity<?> handleImagePathException(ImagePathException ex) {
		return ResponseEntity.internalServerError().body("Invalid image path");
	}
}
