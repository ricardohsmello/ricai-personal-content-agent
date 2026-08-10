package br.com.ricas.web;

import br.com.ricas.model.ChatRequest;
import br.com.ricas.service.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ContentChatController {

	private final ChatService chatService;

	ContentChatController(ChatService chatService) {
		this.chatService = chatService;
	}

	@PostMapping
	public String chat(@RequestBody ChatRequest chatRequest) {
		return chatService.chat(chatRequest);
	}

}
