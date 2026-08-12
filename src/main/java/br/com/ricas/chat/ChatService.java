package br.com.ricas.chat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

	private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

	private final ChatClient chatClient;
	private final ChatMemory chatMemory;

	ChatService(
			@Qualifier("chatClient") ChatClient chatClient,
			ChatMemory chatMemory
	) {
		this.chatClient = chatClient;
		this.chatMemory = chatMemory;
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

	public void recordExchange(ChatRequest request, String answer) {
		chatMemory.add(
				request.conversationId(),
				UserMessage.builder().text(request.message()).build()
		);
		chatMemory.add(
				request.conversationId(),
				AssistantMessage.builder().content(answer).build()
		);
	}
}
