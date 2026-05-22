package com.runningTracker.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;

import com.runningTracker.exception.BusinessValidationException;

@Component
public class UserLocalDateResolver {

	public LocalDate resolveReferenceDate(String userLocalDateHeader) {
		if (userLocalDateHeader == null || userLocalDateHeader.isBlank()) {
			return LocalDate.now();
		}
		try {
			return LocalDate.parse(userLocalDateHeader.trim());
		}
		catch (DateTimeParseException ex) {
			throw new BusinessValidationException("X-User-Local-Date", "Invalid date format, expected YYYY-MM-DD");
		}
	}
}
