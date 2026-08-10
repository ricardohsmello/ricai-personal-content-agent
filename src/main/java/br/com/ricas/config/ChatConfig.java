package br.com.ricas.config;

import br.com.ricas.tools.ContentTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.mongo.MongoChatMemoryRepository;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

	@Bean
	public ChatMemory chatMemory(
			MongoChatMemoryRepository mongoChatMemoryRepository
	) {
		return MessageWindowChatMemory.builder()
				.chatMemoryRepository(mongoChatMemoryRepository)
				.maxMessages(10)
				.build();
	}

	@Bean
	public ChatClient chatClient(OpenAiChatModel openAiChatModel, ChatMemory chatMemory,
			ContentTools contentTools) {
		return ChatClient
				.builder(openAiChatModel)
				.defaultSystem("""
						You are Ricardo Mello's virtual assistant, specialized in his professional
						background, articles, videos, events, talks, and projects.
		
						Select and use the available capabilities that are most appropriate for
						the user's request.
					
						Prefer structured capabilities for filtering, ordering, counting,
						comparison, aggregation, exact lookup, and other operations that require
						deterministic results.
					
						Prefer semantic search capabilities for natural-language information
						retrieval, explanations, summaries, and discovery by meaning or topic.
					
						Use capability results as the source of truth for the operation they
						perform. Do not replace structured results with conclusions inferred from
						semantic similarity.
					
						Base factual answers only on information returned by the available
						capabilities. Never invent facts or use external knowledge.
					
						Reply clearly, respectfully, and in the same language as the user.
					
						If the available capabilities cannot provide the requested information,
						say so briefly and suggest contacting Ricardo at
						ricardohsmello@gmail.com.
				""")
				.defaultAdvisors(
						MessageChatMemoryAdvisor.builder(chatMemory).build()
				)
				.defaultTools(contentTools)
				.build();
	}
}
