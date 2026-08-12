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
                    - Find a talk Ricardo already gave and compare it with a related article.
                    - Find the last article written and create a meeting with Ricardo to discuss the topic.

                    Examples that do not require a plan:
                    - Find my three latest articles.
                    - What was Ricardo's penultimate video?
                    - What was his second most recent article?
                    - What was his first published article?
                    - List the three latest events.
                    - How many videos are available?
                    - Schedule a meeting with Ricardo without first retrieving
                      information needed for the meeting subject.
                    - Explain Ricardo's experience with MongoDB.

                    Scheduling alone does not require a plan. Scheduling does require
                    a plan when it depends on content that must be retrieved first,
                    such as finding the latest event and using it as the meeting topic.

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
                    - Find available Calendly meeting times.
                    - Create a Calendly scheduling link.

                    Rules:
                    - Create between 2 and 5 steps.
                    - Each step must describe one concrete action.
                    - Create a plan only for requests requiring two or more distinct
                      tool calls where a later tool call depends on an earlier result.
                    - Do not turn retrieval, ordinal item selection, formatting,
                      summarization, and final presentation into separate plan steps.
                    - Each plan step must represent one useful tool-backed operation.
                      Topic extraction, result selection, comparison, synthesis, and
                      response writing must be performed within a retrieval step and
                      must never become standalone steps.
                    - First, second, penultimate, latest, newest, oldest, and Nth-item
                      requests are one ordered lookup and must not become a plan.
                    - Read-only steps may retrieve content and Calendly availability.
                    - Before the create-link step, include a read-only step that
                      finds the requested available time and preserves its exact
                      startTime, including the UTC offset.
                    - Preserve user-provided name, email, meeting topic, and time
                      preference in the relevant step instructions.
                    - Do not execute the steps.
                    - Do not invent capabilities.
                    - Preserve quantities and content categories.
                    - For oldest/first or newest/latest requests, explicitly state
                      the required date direction in the step.
                    - For topic requests combined with chronological ordering,
                      keep the topics and OR semantics explicit in the step.
                    - When the user asks about a talk Ricardo gave, presented, or
                      participated in, use past events and explicitly exclude upcoming
                      events. Use upcoming events only when the user clearly asks for
                      future, scheduled, next, or upcoming events.
                    - A plan that relates a past talk to an article must contain
                      exactly two steps: first retrieve past talks with enough detail
                      to identify their technical subjects; then semantically search
                      for related articles and return the strongest pair with the
                      requested comparison in that same final step.
                    - For read-only plans, the final tool-backed step must synthesize
                      and fully answer the user's request.
                    - Never add a follow-up step merely to select, compare, explain,
                      format, or present results.
                    - For scheduling plans, the create-link step is the
                      final step and its tool result is the final answer.
                    - Return the plan in the same language as the user.

                    Example for relating a talk to an article:
                    1. Retrieve past talks by Ricardo, excluding upcoming events, with
                       enough detail to identify their technical subjects.
                    2. Semantically search for articles matching those subjects,
                       select the strongest supported relationship, and directly
                       explain the parallel between the talk and article.
                    """)
                .build();
    }
}
