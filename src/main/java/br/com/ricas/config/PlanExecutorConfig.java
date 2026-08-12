package br.com.ricas.config;

import br.com.ricas.content.ContentTools;
import br.com.ricas.scheduling.SchedulingTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlanExecutorConfig {

    @Bean("planExecutorChatClient")
    public ChatClient planExecutorChatClient(
            OpenAiChatModel chatModel,
            ContentTools contentTools,
            SchedulingTools schedulingTools
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
                        - Do not ask the user whether you should continue, search,
                          compare, or perform a later step. The plan already authorizes
                          every listed read-only operation.
                        - Do not end with offers such as "Posso agora...", "Deseja
                          que eu...?", or equivalent follow-up questions.
                        - For first/oldest content, use findContentByDate with ASC.
                        - For latest content matching topics, use
                          findContentByTopics with DESC; do not approximate this
                          using findRecentContent or semantic search.
                        - For the latest event that already happened, use only
                          findLatestPastEvents. Never use findUpcomingEvents or
                          findContentByDate for that request.
                        - When a request refers to a talk Ricardo gave, presented, or
                          participated in, retrieve only past events. Exclude events
                          whose date is today or in the future.
                        - To relate talks with articles by meaning, use
                          searchKnowledgeBase with a focused query built from the
                          specific technical subjects in previous results. Do not use
                          a broad topic such as MongoDB alone when a more specific
                          subject is available.
                        - Prefer an explicitly supported relationship over a loose
                          shared technology. For example, an article stating that it
                          is based on a particular talk is stronger than two items
                          that merely mention MongoDB.
                        - When the current step is the last comparison step, select
                          the strongest supported talk/article pair and directly
                          explain their shared subject, relationship, and differences.
                          Fully answer the original request instead of asking permission
                          to provide the comparison later.
                        - For companies, employers, roles, job titles, employment
                          periods, or complete career history, use only
                          findProfessionalExperience, not semantic search.
                        - When no exact range was specified, use
                          findNextAvailableMeetingTimes with 31 days. When the user
                          requests the first available time, use limit 1. Never ask
                          for the current date or reject server dates as incorrect.
                        - Preserve the selected time's exact startTime, including
                          its UTC offset, so a later step can use it.
                        - When the current step requests a scheduling link, call
                          createSchedulingLink with the data from the plan and its
                          previous results.
                        - For intermediate steps, return only factual data useful to
                          subsequent steps, without conversational offers or questions.
                        - For the final step, return the complete user-facing answer.
                        """)
                .defaultTools(contentTools, schedulingTools)
                .build();
    }
}
