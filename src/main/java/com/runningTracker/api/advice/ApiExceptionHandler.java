package com.runningTracker.api.advice;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.runningTracker.api.dto.ErrorResponse;
import com.runningTracker.exception.BusinessValidationException;
import com.runningTracker.exception.FieldErrorDetail;
import com.runningTracker.exception.WorkoutNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(BusinessValidationException.class)
	public ResponseEntity<ErrorResponse> handleBusinessValidation(BusinessValidationException ex) {
		return ResponseEntity.badRequest()
			.body(ErrorResponse.validation(ex.getMessage(), ex.getErrors()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		List<FieldErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
			.map(this::toFieldErrorDetail)
			.toList();
		return ResponseEntity.badRequest()
			.body(ErrorResponse.validation("Validation failed", errors));
	}

	@ExceptionHandler(WorkoutNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleWorkoutNotFound(WorkoutNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(ErrorResponse.of(404, ex.getMessage()));
	}

	private FieldErrorDetail toFieldErrorDetail(FieldError fieldError) {
		return new FieldErrorDetail(fieldError.getField(), fieldError.getDefaultMessage());
	}
}
