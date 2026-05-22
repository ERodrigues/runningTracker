package com.runningTracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.runningTracker.api.dto.CreateWorkoutRequest;
import com.runningTracker.exception.BusinessValidationException;
import com.runningTracker.exception.WorkoutNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class WorkoutServiceTest {

	@Autowired
	private WorkoutService workoutService;

	@Test
	void shouldCreateAndFindWorkout() {
		var request = new CreateWorkoutRequest(
			"Long run",
			LocalDate.of(2026, 1, 1),
			new BigDecimal("10.5"),
			"53:00",
			"https://www.strava.com/activities/1"
		);

		var created = workoutService.create(request, "2026-05-22");
		var found = workoutService.findById(created.id());

		assertThat(found.paceMinPerKm()).isEqualByComparingTo(new BigDecimal("5.05"));
		assertThat(found.duration()).isEqualTo("53:00");
	}

	@Test
	void shouldRejectFutureWorkoutDate() {
		var request = new CreateWorkoutRequest(
			"Future run",
			LocalDate.of(2099, 1, 1),
			new BigDecimal("5.0"),
			"30:00",
			null
		);

		assertThatThrownBy(() -> workoutService.create(request, "2026-05-22"))
			.isInstanceOf(BusinessValidationException.class);
	}

	@Test
	void shouldRejectInvalidPageSize() {
		assertThatThrownBy(() -> workoutService.list(0, 101, "workoutDate,desc"))
			.isInstanceOf(BusinessValidationException.class);
	}

	@Test
	void shouldThrowWhenWorkoutNotFound() {
		assertThatThrownBy(() -> workoutService.findById(999L))
			.isInstanceOf(WorkoutNotFoundException.class);
	}
}
