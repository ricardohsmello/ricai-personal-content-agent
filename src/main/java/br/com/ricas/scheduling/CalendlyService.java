package br.com.ricas.scheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CalendlyService {

	private static final int DEFAULT_LIMIT = 5;
	private static final int MAX_LIMIT = 10;
	private static final Duration MAX_AVAILABILITY_PERIOD = Duration.ofDays(31);
	private static final ZoneId DISPLAY_TIMEZONE = ZoneId.of("America/Sao_Paulo");

	private final Logger logger = LoggerFactory.getLogger(CalendlyService.class);

	private final RestClient calendlyClient;
	private final String accessToken;
	private final String eventTypeUri;
	private final String schedulingUrl;

	CalendlyService(
			RestClient.Builder restClientBuilder,
			@Value("${calendly.access-token:}") String accessToken,
			@Value("${calendly.event-type-uri:}") String eventTypeUri,
			@Value("${calendly.scheduling-url:}") String schedulingUrl
	) {
		this.calendlyClient = restClientBuilder
				.baseUrl("https://api.calendly.com")
				.build();
		this.accessToken = accessToken;
		this.eventTypeUri = eventTypeUri;
		this.schedulingUrl = schedulingUrl;
	}

	public List<AvailableMeetingTime> findAvailableTimes(
			String startTime,
			String endTime,
			int limit
	) {
		logger.info("Calling findAvailableTimes calendly");

		validateConfiguration();

		Instant start = Instant.parse(startTime);
		Instant end = Instant.parse(endTime);
		validatePeriod(start, end);
		int effectiveLimit = limit <= 0 ? DEFAULT_LIMIT : Math.min(limit, MAX_LIMIT);

		Map<?, ?> response = calendlyClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/event_type_available_times")
						.queryParam("event_type", eventTypeUri)
						.queryParam("start_time", start)
						.queryParam("end_time", end)
						.build())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.retrieve()
				.body(Map.class);

		return collection(response).stream()
				.limit(effectiveLimit)
				.map(item -> new AvailableMeetingTime(
						toDisplayTime(stringValue(item, "start_time")),
						DISPLAY_TIMEZONE.getId(),
						stringValue(item, "status"),
						stringValue(item, "scheduling_url")
				))
				.toList();
	}

	public List<AvailableMeetingTime> findNextAvailableTimes(
			int daysAhead,
			int limit
	) {
		int effectiveDays = daysAhead <= 0 ? 31 : Math.min(daysAhead, 31);
		Instant start = Instant.now().plusSeconds(5);
		Instant end = start.plus(Duration.ofDays(effectiveDays));

		return findAvailableTimes(start.toString(), end.toString(), limit);
	}

	private String toDisplayTime(String utcStartTime) {
		requireText(utcStartTime, "start_time");
		return Instant.parse(utcStartTime)
				.atZone(DISPLAY_TIMEZONE)
				.toOffsetDateTime()
				.toString();
	}

	public SchedulingLinkResult createSchedulingLink(
			String name,
			String email,
			String meetingDescription,
			String selectedSchedulingUrl
	) {
		logger.info("Calling createSchedulingLink calendly");

		requireText(name, "name");
		requireText(email, "email");
		requireText(meetingDescription, "meetingDescription");
		requireText(schedulingUrl, "calendly.scheduling-url");
		requireText(selectedSchedulingUrl, "selectedSchedulingUrl");
		validateSelectedSchedulingUrl(selectedSchedulingUrl);

		String prefilledUrl = UriComponentsBuilder.fromUriString(selectedSchedulingUrl)
				.queryParam("name", name)
				.queryParam("email", email)
				.queryParam("a1", meetingDescription)
				.build()
				.encode()
				.toUriString();

		return new SchedulingLinkResult(prefilledUrl);
	}

	public SchedulingLinkResult createSchedulingLinkForTime(
			String name,
			String email,
			String meetingDescription,
			String selectedStartTime
	) {
		logger.info("Calling createSchedulingLinkForTime calendly");

		requireText(selectedStartTime, "selectedStartTime");
		Instant selectedInstant = OffsetDateTime.parse(selectedStartTime).toInstant();
		Instant rangeStart = selectedInstant.minusSeconds(60);
		Instant minimumStart = Instant.now().plusSeconds(1);

		if (rangeStart.isBefore(minimumStart)) {
			rangeStart = minimumStart;
		}

		List<AvailableMeetingTime> availableTimes = findAvailableTimes(
				rangeStart.toString(),
				selectedInstant.plusSeconds(60).toString(),
				MAX_LIMIT
		);

		String selectedSchedulingUrl = availableTimes.stream()
				.filter(time -> OffsetDateTime.parse(time.startTime())
						.toInstant()
						.equals(selectedInstant))
				.map(AvailableMeetingTime::schedulingUrl)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(
						"The selected time is no longer available"
				));

		return createSchedulingLink(
				name,
				email,
				meetingDescription,
				selectedSchedulingUrl
		);
	}

	private void validateSelectedSchedulingUrl(String selectedSchedulingUrl) {
		URI configured = URI.create(schedulingUrl);
		URI selected = URI.create(selectedSchedulingUrl);
		String configuredPath = configured.getPath().replaceAll("/+$", "");
		String selectedPath = selected.getPath();

		if (!"https".equalsIgnoreCase(selected.getScheme())
				|| !configured.getHost().equalsIgnoreCase(selected.getHost())
				|| !selectedPath.startsWith(configuredPath + "/")) {
			throw new IllegalArgumentException(
					"selectedSchedulingUrl must be a specific available-time URL returned by Calendly"
			);
		}
	}

	private void validateConfiguration() {
		requireText(accessToken, "calendly.access-token");
		requireText(eventTypeUri, "calendly.event-type-uri");
	}

	private void validatePeriod(Instant start, Instant end) {
		if (!start.isAfter(Instant.now())) {
			throw new IllegalArgumentException("startTime must be in the future");
		}
		if (!end.isAfter(start)) {
			throw new IllegalArgumentException("endTime must be after startTime");
		}
		if (Duration.between(start, end).compareTo(MAX_AVAILABILITY_PERIOD) > 0) {
			throw new IllegalArgumentException("Availability period cannot exceed 31 days");
		}
	}

	private void requireText(String value, String field) {
		if (!StringUtils.hasText(value)) {
			throw new IllegalArgumentException(field + " must be configured or provided");
		}
	}

	private List<Map<?, ?>> collection(Map<?, ?> response) {
		if (response == null || !(response.get("collection") instanceof List<?> values)) {
			return List.of();
		}

		List<Map<?, ?>> collection = new ArrayList<>();
		for (Object value : values) {
			if (value instanceof Map<?, ?> item) {
				collection.add(item);
			}
		}
		return List.copyOf(collection);
	}

	private String stringValue(Map<?, ?> values, String key) {
		Object value = values.get(key);
		return value == null ? null : value.toString();
	}
}
