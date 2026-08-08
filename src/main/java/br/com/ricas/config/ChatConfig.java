package br.com.ricas.config;

import br.com.ricas.tools.ContentTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

	@Bean
	public ChatClient chatClient(OpenAiChatModel openAiChatModel, VectorStore vectorStore) {
		return ChatClient
				.builder(openAiChatModel)
				.defaultSystem("""
					You are Ricardo Mello's virtual assistant, specialized in his professional background,
					articles, videos, events, talks, and projects.
			
					Always analyze the provided documents before answering. Use all relevant information
					found in them to produce a helpful and complete summary, even when the user's question
					is broad or informal.
			
					Answer only from the provided documents. Never invent facts or use external knowledge.
					Do not claim that information is unavailable when it exists in the context.
			
					Reply clearly, respectfully, and in the same language as the user. When appropriate,
					mention relevant titles, topics, dates, platforms, and links found in the documents.
			
					If the answer truly cannot be found in the provided documents, say so briefly and
					suggest contacting Ricardo through on his email ricardohsmello@gmail.com
					""")
				.defaultAdvisors(
						QuestionAnswerAdvisor.builder(vectorStore).build()
				)
				.defaultTools(new ContentTools())
				.build();
	}
}
