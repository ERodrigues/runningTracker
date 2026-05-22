package com.runningTracker.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.runningTracker.exception.BusinessValidationException;

class StravaUrlValidatorTest {

	private StravaUrlValidator validator;

	@BeforeEach
	void setUp() {
		validator = new StravaUrlValidator();
	}

	@Test
	void shouldAcceptAllowedHosts() {
		assertThatCode(() -> validator.validate("https://www.strava.com/activities/1"))
			.doesNotThrowAnyException();
		assertThatCode(() -> validator.validate("http://strava.com/activities/1"))
			.doesNotThrowAnyException();
	}

	@Test
	void shouldRejectOtherHosts() {
		assertThatThrownBy(() -> validator.validate("https://www.google.com"))
			.isInstanceOf(BusinessValidationException.class);
	}

	@Test
	void shouldAllowNullOrBlank() {
		assertThatCode(() -> validator.validate(null)).doesNotThrowAnyException();
		assertThatCode(() -> validator.validate("  ")).doesNotThrowAnyException();
	}
}
