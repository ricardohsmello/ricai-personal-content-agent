package br.com.ricas.chat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ContentChatController {

	private final AgentService agentService;

	ContentChatController(AgentService agentService) {
		this.agentService = agentService;
	}

	@PostMapping
	public String chat(@RequestBody ChatRequest chatRequest) {
		return agentService.respond(chatRequest);
	}

}
