package br.com.ricas.scheduling;

public record AvailableMeetingTime(
		String startTime,
		String timezone,
		String status,
		String schedulingUrl
) {
}
