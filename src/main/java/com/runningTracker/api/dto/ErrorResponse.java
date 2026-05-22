package com.runningTracker.api.dto;

import java.util.List;

import com.runningTracker.exception.FieldErrorDetail;

public record ErrorResponse(int status, String message, List<FieldErrorDetail> errors) {

	public static ErrorResponse of(int status, String message) {
		return new ErrorResponse(status, message, List.of());
	}

	public static ErrorResponse validation(String message, List<FieldErrorDetail> errors) {
		return new ErrorResponse(400, message, errors);
	}
}
