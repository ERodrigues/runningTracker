package com.runningTracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.runningTracker.exception.BusinessValidationException;

class UserLocalDateResolverTest {

	private UserLocalDateResolver resolver;

	@BeforeEach
	void setUp() {
		resolver = new UserLocalDateResolver();
	}

	@Test
	void shouldParseValidHeader() {
		assertThat(resolver.resolveReferenceDate("2026-05-20")).isEqualTo(LocalDate.of(2026, 5, 20));
	}

	@Test
	void shouldUseTodayWhenHeaderMissing() {
		assertThat(resolver.resolveReferenceDate(null)).isEqualTo(LocalDate.now());
	}

	@Test
	void shouldRejectInvalidHeader() {
		assertThatThrownBy(() -> resolver.resolveReferenceDate("20-05-2026"))
			.isInstanceOf(BusinessValidationException.class);
	}
}
