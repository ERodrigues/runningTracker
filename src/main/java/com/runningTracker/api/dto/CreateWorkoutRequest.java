package com.runningTracker.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateWorkoutRequest(
	@NotBlank @Size(max = 120) String title,
	@NotNull LocalDate workoutDate,
	@NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal distanceKm,
	@NotBlank String duration,
	String stravaUrl
) {
}
