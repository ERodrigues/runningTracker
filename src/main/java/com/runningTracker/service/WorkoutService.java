package com.runningTracker.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.runningTracker.api.dto.CreateWorkoutRequest;
import com.runningTracker.api.dto.PagedWorkoutsResponse;
import com.runningTracker.api.dto.WorkoutResponse;
import com.runningTracker.domain.Workout;
import com.runningTracker.exception.BusinessValidationException;
import com.runningTracker.exception.WorkoutNotFoundException;
import com.runningTracker.repository.WorkoutRepository;

@Service
public class WorkoutService {

	private static final int MAX_PAGE_SIZE = 100;
	private static final String SORT_PROPERTY = "workoutDate";

	private final WorkoutRepository workoutRepository;
	private final DurationParser durationParser;
	private final PaceCalculator paceCalculator;
	private final StravaUrlValidator stravaUrlValidator;
	private final UserLocalDateResolver userLocalDateResolver;
	private final WorkoutMapper workoutMapper;

	public WorkoutService(
		WorkoutRepository workoutRepository,
		DurationParser durationParser,
		PaceCalculator paceCalculator,
		StravaUrlValidator stravaUrlValidator,
		UserLocalDateResolver userLocalDateResolver,
		WorkoutMapper workoutMapper
	) {
		this.workoutRepository = workoutRepository;
		this.durationParser = durationParser;
		this.paceCalculator = paceCalculator;
		this.stravaUrlValidator = stravaUrlValidator;
		this.userLocalDateResolver = userLocalDateResolver;
		this.workoutMapper = workoutMapper;
	}

	@Transactional
	public WorkoutResponse create(CreateWorkoutRequest request, String userLocalDateHeader) {
		LocalDate referenceDate = userLocalDateResolver.resolveReferenceDate(userLocalDateHeader);
		if (request.workoutDate().isAfter(referenceDate)) {
			throw new BusinessValidationException("workoutDate", "Workout date cannot be in the future");
		}

		int durationSeconds = durationParser.parseToSeconds(request.duration());
		stravaUrlValidator.validate(request.stravaUrl());

		var pace = paceCalculator.calculate(durationSeconds, request.distanceKm());
		String normalizedTitle = request.title().trim();
		if (normalizedTitle.isEmpty()) {
			throw new BusinessValidationException("title", "Title is required");
		}

		String stravaUrl = normalizeStravaUrl(request.stravaUrl());
		Workout workout = new Workout(
			normalizedTitle,
			request.workoutDate(),
			request.distanceKm(),
			durationSeconds,
			stravaUrl,
			pace
		);
		return workoutMapper.toResponse(workoutRepository.save(workout));
	}

	@Transactional(readOnly = true)
	public WorkoutResponse findById(Long id) {
		return workoutMapper.toResponse(
			workoutRepository.findById(id).orElseThrow(() -> new WorkoutNotFoundException(id))
		);
	}

	@Transactional(readOnly = true)
	public PagedWorkoutsResponse list(int page, int size, String sort) {
		validatePagination(page, size);
		Sort sortOrder = parseSort(sort);
		Pageable pageable = PageRequest.of(page, size, sortOrder);
		Page<Workout> result = workoutRepository.findAll(pageable);
		return new PagedWorkoutsResponse(
			result.map(workoutMapper::toResponse).getContent(),
			result.getNumber(),
			result.getSize(),
			result.getTotalElements(),
			result.getTotalPages()
		);
	}

	private void validatePagination(int page, int size) {
		if (page < 0) {
			throw new BusinessValidationException("page", "Page must be zero or greater");
		}
		if (size < 1) {
			throw new BusinessValidationException("size", "Size must be at least 1");
		}
		if (size > MAX_PAGE_SIZE) {
			throw new BusinessValidationException("size", "Size must not exceed 100");
		}
	}

	private Sort parseSort(String sort) {
		String sortParam = (sort == null || sort.isBlank()) ? "workoutDate,desc" : sort.trim();
		String[] parts = sortParam.split(",");
		if (parts.length != 2) {
			throw new BusinessValidationException("sort", "Sort must be in format workoutDate,asc or workoutDate,desc");
		}
		String property = parts[0].trim();
		String direction = parts[1].trim().toLowerCase();
		if (!SORT_PROPERTY.equals(property)) {
			throw new BusinessValidationException("sort", "Only workoutDate sort is supported");
		}
		Sort.Direction sortDirection = switch (direction) {
			case "asc" -> Sort.Direction.ASC;
			case "desc" -> Sort.Direction.DESC;
			default -> throw new BusinessValidationException("sort", "Sort direction must be asc or desc");
		};
		return Sort.by(
			new Sort.Order(sortDirection, SORT_PROPERTY),
			Sort.Order.desc("id")
		);
	}

	private String normalizeStravaUrl(String stravaUrl) {
		if (stravaUrl == null || stravaUrl.isBlank()) {
			return null;
		}
		return stravaUrl.trim();
	}
}
