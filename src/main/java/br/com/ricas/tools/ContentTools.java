package br.com.ricas.tools;

import br.com.ricas.model.CatalogContentResult;
import br.com.ricas.model.ContentResult;
import br.com.ricas.service.ContentKbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ContentTools {

	private static final Logger logger =
			LoggerFactory.getLogger(ContentTools.class);

	@Tool(description = "Get the current date")
	public Date getDate() {
		logger.info("Calling getDate");

		return new Date();
	}

	private final ContentKbService contentKbService;

	ContentTools(ContentKbService contentKbService) {
		this.contentKbService = contentKbService;
	}

	@Tool(description = """
			Retrieves content from Ricardo Mello's catalog by category, ordered by
			publication date from newest to oldest. Returns up to the requested number
			of results. Suitable for chronological and recent-content lookups.
			""")
	public List<CatalogContentResult> findRecentContent(
			@ToolParam(description = "Content category: article, video, event, or project. Use event for talks")
			String category,
			@ToolParam(description = "Number of items: 1 for singular requests, or 5 when unspecified")
			int limit
	) {

		return contentKbService.findByMetadataCategory(category.toLowerCase(), limit);
	}

	@Tool(description = """
        Searches Ricardo Mello's knowledge base using semantic similarity.

        Use this capability to discover information by meaning or topic,
        including professional background, technologies, articles, videos,
        events, talks, and projects.

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
		return contentKbService.searchKnowledgeBase(query, limit);
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
		return contentKbService.countByMetadataCategory(category);
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
		return contentKbService.findUpcomingEvents(limit);
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
		return contentKbService.findContentByPeriod(category, startDate, endDate, limit);
	}

}
