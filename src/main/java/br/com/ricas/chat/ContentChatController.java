package br.com.ricas.chat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@RestController
@RequestMapping("/chat")
public class ContentChatController {
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter
			.ofPattern("HH:mm:ss")
			.withZone(ZoneId.of("America/Sao_Paulo"));

	private final AgentService agentService;
	private final ConversationRateLimitService rateLimitService;

	ContentChatController(
			AgentService agentService,
			ConversationRateLimitService rateLimitService
	) {
		this.agentService = agentService;
		this.rateLimitService = rateLimitService;
	}

	@PostMapping
	public ResponseEntity<String> chat(@RequestBody ChatRequest chatRequest) {
		String conversationId = chatRequest.conversationId();
		if (conversationId == null || conversationId.isBlank() || conversationId.length() > 100) {
			return ResponseEntity.status(BAD_REQUEST).body("Invalid conversationId.");
		}

		ConversationRateLimitService.RateLimitResult limit =
				rateLimitService.check(conversationId);
		if (!limit.allowed()) {
			String availableAt = TIME_FORMATTER.format(limit.availableAt());
			return ResponseEntity.status(TOO_MANY_REQUESTS)
					.header("Retry-After", Long.toString(limit.retryAfterSeconds()))
					.body("Rate limit exceeded: maximum %d requests per minute. Try again at %s."
							.formatted(limit.maxRequests(), availableAt));
		}

		return ResponseEntity.ok(agentService.respond(chatRequest));
	}

}
