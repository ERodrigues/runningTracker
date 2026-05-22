package com.runningTracker.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "workout", indexes = @Index(columnList = "workout_date, id"))
public class Workout {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 120)
	private String title;

	@Column(name = "workout_date", nullable = false)
	private LocalDate workoutDate;

	@Column(name = "distance_km", nullable = false, precision = 19, scale = 4)
	private BigDecimal distanceKm;

	@Column(name = "duration_seconds", nullable = false)
	private int durationSeconds;

	@Column(name = "strava_url", length = 2048)
	private String stravaUrl;

	@Column(name = "pace_min_per_km", nullable = false, precision = 10, scale = 2)
	private BigDecimal paceMinPerKm;

	protected Workout() {
	}

	public Workout(String title, LocalDate workoutDate, BigDecimal distanceKm, int durationSeconds,
			String stravaUrl, BigDecimal paceMinPerKm) {
		this.title = title;
		this.workoutDate = workoutDate;
		this.distanceKm = distanceKm;
		this.durationSeconds = durationSeconds;
		this.stravaUrl = stravaUrl;
		this.paceMinPerKm = paceMinPerKm;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public LocalDate getWorkoutDate() {
		return workoutDate;
	}

	public BigDecimal getDistanceKm() {
		return distanceKm;
	}

	public int getDurationSeconds() {
		return durationSeconds;
	}

	public String getStravaUrl() {
		return stravaUrl;
	}

	public BigDecimal getPaceMinPerKm() {
		return paceMinPerKm;
	}
}
