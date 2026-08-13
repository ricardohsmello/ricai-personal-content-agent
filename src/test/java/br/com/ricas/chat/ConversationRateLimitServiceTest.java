package br.com.ricas.chat;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class ConversationRateLimitServiceTest {

	@Test
	void rejectsTheSixthRequestInTheSameMinute() {
		ConversationRateLimitService service = new ConversationRateLimitService(
				Clock.fixed(Instant.parse("2026-08-13T12:00:00Z"), ZoneOffset.UTC)
		);

		for (int request = 0; request < 5; request++) {
			assertThat(service.check("conversation-1").allowed()).isTrue();
		}

		ConversationRateLimitService.RateLimitResult result =
				service.check("conversation-1");

		assertThat(result.allowed()).isFalse();
		assertThat(result.retryAfterSeconds()).isEqualTo(60);
		assertThat(result.maxRequests()).isEqualTo(5);
		assertThat(result.availableAt()).isEqualTo(
				Instant.parse("2026-08-13T12:01:00Z")
		);
	}

	@Test
	void tracksConversationsIndependently() {
		ConversationRateLimitService service = new ConversationRateLimitService(5);

		for (int request = 0; request < 5; request++) {
			service.check("conversation-1");
		}

		assertThat(service.check("conversation-2").allowed()).isTrue();
	}
}
