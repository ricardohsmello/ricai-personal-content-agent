package br.com.ricas.web;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ContentChatController {

	private final ChatClient chatClient;

	ContentChatController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	public String chat(@RequestBody String message) {
		return chatClient.prompt(message)
				.call()
				.content();
	}
}
