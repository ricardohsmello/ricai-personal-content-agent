package br.com.ricas.web;

import br.com.ricas.model.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
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
	public String chat(@RequestBody ChatRequest chatRequest) {
		logger.info("Message: " + chatRequest);

		return chatClient.prompt(chatRequest.message())
				.advisors(advisor -> advisor
						.param(ChatMemory.CONVERSATION_ID, chatRequest.conversationId()))
				.call()
				.content();
	}


}
