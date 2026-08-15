package br.com.ricas.content;

import br.com.ricas.tools.ToolUsageTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ContentTools {

	private static final Logger logger =
			LoggerFactory.getLogger(ContentTools.class);

	private final KnowledgeBaseService knowledgeBaseService;
	private final ToolUsageTracker toolUsageTracker;

	ContentTools(
			KnowledgeBaseService knowledgeBaseService,
			ToolUsageTracker toolUsageTracker
	) {
		this.knowledgeBaseService = knowledgeBaseService;
		this.toolUsageTracker = toolUsageTracker;
	}

	@Tool(description = "Get the current date")
	public Date getDate() {
		toolUsageTracker.record("getDate");
		logger.info("Calling getDate");

		return new Date();
	}

	@Tool(description = """
			Retrieves content from Ricardo Mello's catalog by category, ordered by
			publication date from newest to oldest. Returns up to the requested number
			of results. Use only for unfiltered recent-content lookups. Do not use it
			for oldest/first content or for content filtered by topic.
			""")
	public List<CatalogContentResult> findRecentContent(
			@ToolParam(description = "Content category: article, video, event, or project. Use event for talks")
			String category,
			@ToolParam(description = "Number of items: 1 for singular requests, or 5 when unspecified")
			int limit
	) {
		toolUsageTracker.record("findRecentContent");
		return knowledgeBaseService.findByMetadataCategory(category.toLowerCase(), limit);
	}

	@Tool(description = """
			Retrieves Ricardo Mello's complete professional employment history,
			ordered from the most recent experience to the oldest. Use this capability
			for exact questions about every company he worked for, previous employers,
			job titles, roles, employment periods, responsibilities, or career history.
			This structured capability returns all experience records and must be used
			instead of semantic search for complete or exhaustive employment lists.
			""")
	public List<CatalogContentResult> findProfessionalExperience() {
		toolUsageTracker.record("findProfessionalExperience");
		return knowledgeBaseService.findProfessionalExperience();
	}

	@Tool(description = """
			Retrieves content from Ricardo Mello's catalog by category and publication
			date. Use ASC to find the oldest or first published content. Use DESC to
			find the newest or latest content. This operation is deterministic.
			""")
	public List<CatalogContentResult> findContentByDate(
			@ToolParam(description = "Content category: article, video, event, or project")
			String category,
			@ToolParam(description = "Date direction: ASC for oldest/first, DESC for newest/latest")
			String direction,
			@ToolParam(description = "Maximum number of results; use 1 for singular requests")
			int limit
	) {
		toolUsageTracker.record("findContentByDate");
		return knowledgeBaseService.findByCategoryOrderedByDate(
				category,
				parseDirection(direction),
				limit
		);
	}

	@Tool(description = """
			Retrieves content matching any of the supplied topics, filtered by exact
			category and ordered by publication date. Topic matching uses OR semantics.
			Use this for requests such as the latest articles about AI or Kotlin.
			Use ASC for oldest and DESC for newest. This is preferable to semantic
			search when chronological ordering must be guaranteed.
			""")
	public List<CatalogContentResult> findContentByTopics(
			@ToolParam(description = "Content category: article, video, event, or project")
			String category,
			@ToolParam(description = "Topics to match using OR semantics, for example [AI, Kotlin]")
			List<String> topics,
			@ToolParam(description = "Date direction: ASC for oldest, DESC for newest")
			String direction,
			@ToolParam(description = "Maximum number of results")
			int limit
	) {
		toolUsageTracker.record("findContentByTopics");
		return knowledgeBaseService.findByTopicsOrderedByDate(
				category,
				topics,
				parseDirection(direction),
				limit
		);
	}

	private Sort.Direction parseDirection(String direction) {
		try {
			return Sort.Direction.fromString(direction);
		}
		catch (IllegalArgumentException exception) {
			throw new IllegalArgumentException(
					"direction must be ASC or DESC",
					exception
			);
		}
	}

	@Tool(description = """
        Searches Ricardo Mello's knowledge base using semantic similarity.

        Use this capability to discover information by meaning or topic,
        including professional background, technologies, articles, videos,
		events, talks, projects, contact information, and social-network profiles.
		Always use it for questions about how to contact Ricardo or requests for
		his email, GitHub, LinkedIn, Twitter/X, Medium, Foojay.io, YouTube, Dev.to,
		Calendly, social profiles, handles, or profile links.

        This search is suitable for explanations, summaries, topic discovery,
        and natural-language questions. It is not suitable for chronological
        ordering, counting, or exact aggregation.
        """)
	public List<ContentResult> searchKnowledgeBase(
			@ToolParam(description = """
                A focused semantic search query representing the information
                the user wants to find
                """)
			String query,

			@ToolParam(description = """
                Maximum number of relevant documents to return. Use 3 when unspecified
                """)
			int limit
	) {
		toolUsageTracker.record("searchKnowledgeBase");
		return knowledgeBaseService.searchKnowledgeBase(query, limit);
	}

	@Tool(description = """
    Counts content in Ricardo Mello's catalog for a given category.

    Suitable for exact quantitative questions. This capability performs an
    exact structured count and does not use semantic similarity.
    """)
	public long countContent(
			@ToolParam(
					description = "Content category: article, video, event, or project. Use event for talks"
			)
			String category
	) {
		toolUsageTracker.record("countContent");
		return knowledgeBaseService.countByMetadataCategory(category);
	}

	@Tool(description = """
    Retrieves Ricardo Mello's upcoming events and talks, ordered from the
    nearest upcoming event to the most distant.

    Suitable for questions about next, upcoming, or future events and talks.
    """)
	public List<CatalogContentResult> findUpcomingEvents(
			@ToolParam(
					description = "Maximum number of upcoming events to return. Use 5 when unspecified"
			)
			int limit
	) {
		toolUsageTracker.record("findUpcomingEvents");
		return knowledgeBaseService.findUpcomingEvents(limit);
	}

	@Tool(description = """
			Retrieves events and talks that already happened, ordered from the most
			recent past event to the oldest. Use this for requests about Ricardo's
			latest, last, or most recent event that has already occurred. It excludes
			today and all future events. Do not combine findUpcomingEvents with
			findContentByDate for this purpose.
			""")
	public List<CatalogContentResult> findLatestPastEvents(
			@ToolParam(description = "Maximum number of past events; use 1 for the latest event")
			int limit
	) {
		toolUsageTracker.record("findLatestPastEvents");
		return knowledgeBaseService.findLatestPastEvents(limit);
	}

	@Tool(description = """
    Retrieves Ricardo Mello's content published or scheduled within an exact
    date interval, ordered from newest to oldest.

    Suitable for date-range and period-based catalog queries.
    """)
	public List<CatalogContentResult> findContentByPeriod(
			@ToolParam(
					description = "Content category: article, video, event, or project. Use event for talks"
			)
			String category,

			@ToolParam(
					description = "Inclusive start date in ISO-8601 format: YYYY-MM-DD"
			)
			String startDate,

			@ToolParam(
					description = "Inclusive end date in ISO-8601 format: YYYY-MM-DD"
			)
			String endDate,

			@ToolParam(
					description = "Maximum number of results to return. Use 5 when unspecified"
			)
			int limit
	) {
		toolUsageTracker.record("findContentByPeriod");
		return knowledgeBaseService.findContentByPeriod(category, startDate, endDate, limit);
	}

}
