package com.runningTracker.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record WorkoutResponse(
	Long id,
	String title,
	LocalDate workoutDate,
	BigDecimal distanceKm,
	String duration,
	int durationSeconds,
	String stravaUrl,
	BigDecimal paceMinPerKm
) {
}
