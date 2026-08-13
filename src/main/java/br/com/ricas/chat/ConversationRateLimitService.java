package br.com.ricas.chat;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ConversationRateLimitService {

	private static final Duration WINDOW = Duration.ofMinutes(1);

	private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();
	private final Clock clock;
	private final int maxRequests;

	@Autowired
	public ConversationRateLimitService(
			@Value("${agent.rate-limit.max-requests-per-minute}") int maxRequests
	) {
		this(Clock.systemUTC(), maxRequests);
	}

	ConversationRateLimitService(Clock clock) {
		this(clock, 5);
	}

	ConversationRateLimitService(Clock clock, int maxRequests) {
		this.clock = clock;
		this.maxRequests = maxRequests;
	}

	public RateLimitResult check(String conversationId) {
		long now = clock.millis();
		AtomicReference<RateLimitResult> result = new AtomicReference<>();

		windows.compute(conversationId, (key, current) -> {
			if (current == null || now - current.startedAt() >= WINDOW.toMillis()) {
				result.set(RateLimitResult.permitted(maxRequests));
				return new Window(1, now);
			}

			if (current.requests() >= maxRequests) {
				long remainingMillis = WINDOW.toMillis() - (now - current.startedAt());
				long retryAfterSeconds = Math.max(1, (remainingMillis + 999) / 1_000);
				result.set(RateLimitResult.denied(
						retryAfterSeconds,
						maxRequests,
						Instant.ofEpochMilli(current.startedAt() + WINDOW.toMillis())
				));
				return current;
			}

			result.set(RateLimitResult.permitted(maxRequests));
			return new Window(current.requests() + 1, current.startedAt());
		});

		return result.get();
	}

	public record RateLimitResult(
			boolean allowed,
			long retryAfterSeconds,
			int maxRequests,
			Instant availableAt
	) {
		private static RateLimitResult permitted(int maxRequests) {
			return new RateLimitResult(true, 0, maxRequests, null);
		}

		private static RateLimitResult denied(
				long retryAfterSeconds,
				int maxRequests,
				Instant availableAt
		) {
			return new RateLimitResult(
					false,
					retryAfterSeconds,
					maxRequests,
					availableAt
			);
		}
	}

	private record Window(int requests, long startedAt) {
	}
}
