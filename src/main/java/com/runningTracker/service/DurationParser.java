package com.runningTracker.service;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.runningTracker.exception.BusinessValidationException;

@Component
public class DurationParser {

	private static final Pattern DURATION_PATTERN = Pattern.compile("^(\\d{1,3}):([0-5]\\d)$");

	public int parseToSeconds(String duration) {
		if (duration == null || duration.isBlank()) {
			throw new BusinessValidationException("duration", "Duration is required");
		}
		var matcher = DURATION_PATTERN.matcher(duration.trim());
		if (!matcher.matches()) {
			throw new BusinessValidationException("duration", "Invalid mm:ss format");
		}
		int minutes = Integer.parseInt(matcher.group(1));
		int seconds = Integer.parseInt(matcher.group(2));
		int totalSeconds = minutes * 60 + seconds;
		if (totalSeconds <= 0) {
			throw new BusinessValidationException("duration", "Duration must be greater than zero");
		}
		return totalSeconds;
	}

	public String format(int totalSeconds) {
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;
		return "%d:%02d".formatted(minutes, seconds);
	}
}
