package com.runningTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runningTracker.domain.Workout;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
}
