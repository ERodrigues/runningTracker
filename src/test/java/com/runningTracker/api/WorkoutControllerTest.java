package com.runningTracker.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.runningTracker.api.advice.ApiExceptionHandler;
import com.runningTracker.api.dto.CreateWorkoutRequest;
import com.runningTracker.api.dto.PagedWorkoutsResponse;
import com.runningTracker.api.dto.WorkoutResponse;
import com.runningTracker.exception.WorkoutNotFoundException;
import com.runningTracker.service.WorkoutService;

@WebMvcTest(WorkoutController.class)
@Import(ApiExceptionHandler.class)
class WorkoutControllerTest {

	@Autowired
	private org.springframework.test.web.servlet.MockMvc mockMvc;

	@MockitoBean
	private WorkoutService workoutService;

	@Test
	void shouldCreateWorkout() throws Exception {
		when(workoutService.create(any(CreateWorkoutRequest.class), eq(null)))
			.thenReturn(sampleResponse());

		mockMvc.perform(post("/v1/workouts")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "title": "Long run",
					  "workoutDate": "2026-05-20",
					  "distanceKm": 10.5,
					  "duration": "53:00"
					}
					"""))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.paceMinPerKm").value(5.05));
	}

	@Test
	void shouldReturnWorkoutById() throws Exception {
		when(workoutService.findById(1L)).thenReturn(sampleResponse());

		mockMvc.perform(get("/v1/workouts/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Long run"));
	}

	@Test
	void shouldReturnNotFound() throws Exception {
		when(workoutService.findById(99L)).thenThrow(new WorkoutNotFoundException(99L));

		mockMvc.perform(get("/v1/workouts/99"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.status").value(404));
	}

	@Test
	void shouldListWorkouts() throws Exception {
		when(workoutService.list(0, 20, "workoutDate,desc"))
			.thenReturn(new PagedWorkoutsResponse(List.of(sampleResponse()), 0, 20, 1, 1));

		mockMvc.perform(get("/v1/workouts"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].id").value(1));
	}

	private WorkoutResponse sampleResponse() {
		return new WorkoutResponse(
			1L,
			"Long run",
			LocalDate.of(2026, 5, 20),
			new BigDecimal("10.5"),
			"53:00",
			3180,
			null,
			new BigDecimal("5.05")
		);
	}
}
