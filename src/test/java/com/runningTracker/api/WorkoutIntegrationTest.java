package com.runningTracker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WorkoutIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldCreateGetAndListWorkout() throws Exception {
		mockMvc.perform(post("/v1/workouts")
				.header("X-User-Local-Date", "2026-05-22")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					  "title": "Long run",
					  "workoutDate": "2026-05-20",
					  "distanceKm": 10.5,
					  "duration": "53:00",
					  "stravaUrl": "https://www.strava.com/activities/123456"
					}
					"""))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.paceMinPerKm").value(5.05))
			.andExpect(jsonPath("$.id").exists());

		mockMvc.perform(get("/v1/workouts/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.duration").value("53:00"));

		mockMvc.perform(get("/v1/workouts"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].title").value("Long run"));
	}
}
