package com.runningTracker.exception;

import java.util.List;

public class BusinessValidationException extends RuntimeException {

	private final List<FieldErrorDetail> errors;

	public BusinessValidationException(String field, String message) {
		this(List.of(new FieldErrorDetail(field, message)));
	}

	public BusinessValidationException(List<FieldErrorDetail> errors) {
		super("Validation failed");
		this.errors = List.copyOf(errors);
	}

	public List<FieldErrorDetail> getErrors() {
		return errors;
	}
}
