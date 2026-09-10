package br.com.ricas.chat;
import br.com.ricas.plan.TaskPlanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	private final TaskPlanService taskPlanService;

	ContentChatController(
			AgentService agentService,
			ConversationRateLimitService rateLimitService,
			TaskPlanService taskPlanService
	) {
		this.agentService = agentService;
		this.rateLimitService = rateLimitService;
		this.taskPlanService = taskPlanService;
	}

	@GetMapping("/progress/{conversationId}")
	public ResponseEntity<AgentProgressResponse> progress(
			@PathVariable String conversationId
	) {
		if (invalidConversationId(conversationId)) {
			return ResponseEntity.badRequest().build();
		}

		return taskPlanService.findLatest(conversationId)
				.map(AgentProgressResponse::from)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.noContent().build());
	}

	@PostMapping
	public ResponseEntity<String> chat(@RequestBody ChatRequest chatRequest) {
		String conversationId = chatRequest.conversationId();
		if (invalidConversationId(conversationId)) {
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

	private boolean invalidConversationId(String conversationId) {
		return conversationId == null
				|| conversationId.isBlank()
				|| conversationId.length() > 100;
	}

}
