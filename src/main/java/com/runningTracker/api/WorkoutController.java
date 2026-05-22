package com.runningTracker.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.runningTracker.api.dto.CreateWorkoutRequest;
import com.runningTracker.api.dto.PagedWorkoutsResponse;
import com.runningTracker.api.dto.WorkoutResponse;
import com.runningTracker.service.WorkoutService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/workouts")
public class WorkoutController {

	private final WorkoutService workoutService;

	public WorkoutController(WorkoutService workoutService) {
		this.workoutService = workoutService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public WorkoutResponse create(
		@Valid @RequestBody CreateWorkoutRequest request,
		@RequestHeader(value = "X-User-Local-Date", required = false) String userLocalDate
	) {
		return workoutService.create(request, userLocalDate);
	}

	@GetMapping("/{id}")
	public WorkoutResponse getById(@PathVariable Long id) {
		return workoutService.findById(id);
	}

	@GetMapping
	public PagedWorkoutsResponse list(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(defaultValue = "workoutDate,desc") String sort
	) {
		return workoutService.list(page, size, sort);
	}
}
