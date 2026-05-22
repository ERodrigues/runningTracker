package com.runningTracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.runningTracker.exception.BusinessValidationException;

class DurationParserTest {

	private DurationParser durationParser;

	@BeforeEach
	void setUp() {
		durationParser = new DurationParser();
	}

	@Test
	void shouldParseMmSsToSeconds() {
		assertThat(durationParser.parseToSeconds("53:00")).isEqualTo(3180);
		assertThat(durationParser.parseToSeconds("5:30")).isEqualTo(330);
	}

	@Test
	void shouldFormatSecondsToMmSs() {
		assertThat(durationParser.format(3180)).isEqualTo("53:00");
		assertThat(durationParser.format(330)).isEqualTo("5:30");
	}

	@Test
	void shouldRejectInvalidFormats() {
		assertThatThrownBy(() -> durationParser.parseToSeconds("0:00"))
			.isInstanceOf(BusinessValidationException.class);
		assertThatThrownBy(() -> durationParser.parseToSeconds("5:60"))
			.isInstanceOf(BusinessValidationException.class);
		assertThatThrownBy(() -> durationParser.parseToSeconds("abc"))
			.isInstanceOf(BusinessValidationException.class);
	}
}
