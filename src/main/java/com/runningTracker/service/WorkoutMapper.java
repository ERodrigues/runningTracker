package com.runningTracker.service;

import org.springframework.stereotype.Component;

import com.runningTracker.api.dto.WorkoutResponse;
import com.runningTracker.domain.Workout;

@Component
public class WorkoutMapper {

	private final DurationParser durationParser;

	public WorkoutMapper(DurationParser durationParser) {
		this.durationParser = durationParser;
	}

	public WorkoutResponse toResponse(Workout workout) {
		return new WorkoutResponse(
			workout.getId(),
			workout.getTitle(),
			workout.getWorkoutDate(),
			workout.getDistanceKm(),
			durationParser.format(workout.getDurationSeconds()),
			workout.getDurationSeconds(),
			workout.getStravaUrl(),
			workout.getPaceMinPerKm()
		);
	}
}
