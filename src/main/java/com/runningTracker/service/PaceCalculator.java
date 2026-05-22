package com.runningTracker.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class PaceCalculator {

	public BigDecimal calculate(int durationSeconds, BigDecimal distanceKm) {
		BigDecimal minutes = BigDecimal.valueOf(durationSeconds)
			.divide(BigDecimal.valueOf(60), 10, RoundingMode.HALF_UP);
		return minutes.divide(distanceKm, 2, RoundingMode.HALF_UP);
	}
}
