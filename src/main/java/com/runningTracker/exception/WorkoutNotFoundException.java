package com.runningTracker.exception;

public class WorkoutNotFoundException extends RuntimeException {

	public WorkoutNotFoundException(Long id) {
		super("Workout not found");
	}
}
