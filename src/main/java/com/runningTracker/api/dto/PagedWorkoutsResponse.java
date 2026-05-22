package com.runningTracker.api.dto;

import java.util.List;

public record PagedWorkoutsResponse(
	List<WorkoutResponse> content,
	int page,
	int size,
	long totalElements,
	int totalPages
) {
}
