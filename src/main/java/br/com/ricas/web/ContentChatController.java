package br.com.ricas.web;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/chat")
public class ContentChatController {

	private final Logger logger = Logger.getLogger(ContentChatController.class.getName());
	private final ChatClient chatClient;

	ContentChatController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	public String chat(@RequestBody String message) {
		logger.info("Message: " + message);

		return chatClient.prompt(message)
				.call()
				.content();
	}
}
