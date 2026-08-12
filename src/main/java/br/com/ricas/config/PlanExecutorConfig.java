package br.com.ricas.config;

import br.com.ricas.tools.ContentTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlanExecutorConfig {

    @Bean("planExecutorChatClient")
    public ChatClient planExecutorChatClient(
            OpenAiChatModel chatModel,
            ContentTools contentTools
    ) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        You execute exactly one step of a previously created plan.

                        Rules:
                        - Execute only the current step.
                        - Use the available tools when necessary.
                        - Previous step results are context and source of truth.
                        - Do not execute future steps.
                        - Do not change the plan.
                        - Do not invent information.
                        - For first/oldest content, use findContentByDate with ASC.
                        - For latest content matching topics, use
                          findContentByTopics with DESC; do not approximate this
                          using findRecentContent or semantic search.
                        - For the latest event that already happened, use only
                          findLatestPastEvents. Never use findUpcomingEvents or
                          findContentByDate for that request.
                        - For companies, employers, roles, job titles, employment
                          periods, or complete career history, use only
                          findProfessionalExperience, not semantic search.
                        - Return only the result of the current step.
                        """)
                .defaultTools(contentTools)
                .build();
    }
}
