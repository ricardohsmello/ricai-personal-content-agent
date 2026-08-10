package br.com.ricas.service;

import br.com.ricas.model.ChatRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

	private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

	private final ChatClient chatClient;

	ChatService(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	public String chat(ChatRequest chatRequest) {
		long start = System.nanoTime();

		logger.info("Message: {}", chatRequest);

		try {
			return chatClient.prompt(chatRequest.message())
					.advisors(advisor -> advisor
							.param(ChatMemory.CONVERSATION_ID, chatRequest.conversationId()))
					.call()
					.content();
		}
		finally {
			long durationMs = (System.nanoTime() - start) / 1_000_000;
			logger.info("Chat operation completed in {} ms", durationMs);
		}
	}
}
