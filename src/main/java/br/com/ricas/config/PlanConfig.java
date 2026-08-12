package br.com.ricas.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlanConfig {

    @Bean("planRouterChatClient")
    public ChatClient planRouterChatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                    Decide whether a user request needs an explicit execution plan.

                    A plan is required only when the request requires two or more
                    distinct tool calls and a later tool call needs data returned
                    by an earlier tool call.

                    Count external tool calls, not reasoning, selection, formatting,
                    comparison, or response-writing steps. A request does not require
                    a plan when one tool call can retrieve all required data, even if
                    the model must select an item, format it, summarize it, or return
                    a specific position from the resulting list.

                    Ordinal requests such as first, second, penultimate, latest,
                    newest, oldest, or the Nth item are single operations and do not
                    require a plan. They can be answered with one ordered content
                    lookup using an adequate limit.

                    Examples that require a plan:
                    - Find recent events about AI and relate articles to each event.
                    - Find recent articles about Java and compare their topics.

                    Examples that do not require a plan:
                    - Find my three latest articles.
                    - What was Ricardo's penultimate video?
                    - What was his second most recent article?
                    - What was his first published article?
                    - List the three latest events.
                    - How many videos are available?
                    - Schedule a meeting with Ricardo.
                    - Find an event and then schedule a meeting about it.
                    - Explain Ricardo's experience with MongoDB.

                    Any request involving scheduling, booking, Calendly, a call,
                    or another external side effect must return requiresPlan=false.
                    The regular assistant handles those requests with confirmation.

                    When uncertain, return requiresPlan=false. Do not create a plan
                    merely to split retrieval, item selection, and final presentation
                    into separate steps.

                    Return requiresPlan and a short reason. Do not answer the request.
                    """)
                .build();
    }

    @Bean("planChatClient")
    public ChatClient planChatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                    You are a planning component for Ricardo Mello's
                    personal content assistant.

                    Convert the user's compound request into a short,
                    ordered execution plan.

                    Available capabilities:
                    - Search content by semantic similarity.
                    - Find content by category ordered by date, including the
                      oldest/first or newest/latest content.
                    - Find chronologically ordered content matching any of
                      multiple topics using OR semantics.
                    - Count content by category.
                    - Retrieve Ricardo's complete professional employment history.
                    - Find upcoming events.
                    - Find the latest events that already happened.
                    - Find content within a date range.

                    Rules:
                    - Create between 2 and 5 steps.
                    - Each step must describe one concrete action.
                    - Create a plan only for requests requiring two or more distinct
                      tool calls where a later tool call depends on an earlier result.
                    - Do not turn retrieval, ordinal item selection, formatting,
                      summarization, and final presentation into separate plan steps.
                    - First, second, penultimate, latest, newest, oldest, and Nth-item
                      requests are one ordered lookup and must not become a plan.
                    - Use only the available read-only content capabilities.
                    - Do not create scheduling or external side-effect steps.
                    - Do not execute the steps.
                    - Do not invent capabilities.
                    - Preserve quantities and content categories.
                    - For oldest/first or newest/latest requests, explicitly state
                      the required date direction in the step.
                    - For topic requests combined with chronological ordering,
                      keep the topics and OR semantics explicit in the step.
                    - The final step must synthesize the final user-facing answer
                      from the previous step results and must not require a tool.
                    - Return the plan in the same language as the user.
                    """)
                .build();
    }
}
