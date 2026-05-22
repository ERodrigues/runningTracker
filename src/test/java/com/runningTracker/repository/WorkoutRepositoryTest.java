package com.runningTracker.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.runningTracker.domain.Workout;

@DataJpaTest
@ActiveProfiles("test")
class WorkoutRepositoryTest {

	@Autowired
	private WorkoutRepository workoutRepository;

	@Test
	void shouldPersistAndFindWorkout() {
		Workout workout = new Workout(
			"Long run",
			LocalDate.of(2026, 5, 20),
			new BigDecimal("10.5"),
			3180,
			null,
			new BigDecimal("5.05")
		);

		Workout saved = workoutRepository.save(workout);

		assertThat(saved.getId()).isNotNull();
		assertThat(workoutRepository.findById(saved.getId()))
			.get()
			.extracting(Workout::getTitle, Workout::getDistanceKm)
			.containsExactly("Long run", new BigDecimal("10.5"));
	}
}
