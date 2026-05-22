package com.runningTracker.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class PaceCalculatorTest {

	private final PaceCalculator paceCalculator = new PaceCalculator();

	@Test
	void shouldCalculatePaceMinPerKm() {
		BigDecimal pace = paceCalculator.calculate(3180, new BigDecimal("10.5"));
		assertThat(pace).isEqualByComparingTo(new BigDecimal("5.05"));
	}
}
