package br.com.ricas.model;

public record AvailableMeetingTime(
		String startTime,
		String timezone,
		String status,
		String schedulingUrl
) {
}
