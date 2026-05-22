package com.runningTracker.service;

import java.net.URI;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.runningTracker.exception.BusinessValidationException;

@Component
public class StravaUrlValidator {

	private static final Set<String> ALLOWED_HOSTS = Set.of("strava.com", "www.strava.com");

	public void validate(String stravaUrl) {
		if (stravaUrl == null || stravaUrl.isBlank()) {
			return;
		}
		try {
			URI uri = URI.create(stravaUrl.trim());
			String scheme = uri.getScheme();
			if (scheme == null || (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))) {
				throw invalid();
			}
			String host = uri.getHost();
			if (host == null || !ALLOWED_HOSTS.contains(host.toLowerCase())) {
				throw invalid();
			}
		}
		catch (IllegalArgumentException ex) {
			throw invalid();
		}
	}

	private BusinessValidationException invalid() {
		return new BusinessValidationException("stravaUrl", "Strava URL must use host strava.com or www.strava.com");
	}
}
