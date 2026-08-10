package br.com.ricas.tools;

import br.com.ricas.model.CatalogContentResult;
import br.com.ricas.model.ContentResult;
import br.com.ricas.repository.ContentKbRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

	private static final int DEFAULT_LIMIT = 5;
	private static final int MAX_LIMIT = 10;

	private final ContentKbRepository contentKbRepository;
	private final VectorStore vectorStore;

	ContentTools(ContentKbRepository contentKbRepository, VectorStore vectorStore) {
		this.contentKbRepository = contentKbRepository;
		this.vectorStore = vectorStore;
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
		int effectiveLimit = limit <= 0 ? DEFAULT_LIMIT : Math.min(limit, MAX_LIMIT);
		var pageable = PageRequest.of(
				0,
				effectiveLimit,
				Sort.by(Sort.Direction.DESC, "metadata.createdAt")
		);

		logger.info("Calling findByMetadataCategory with params {}, {}", category, pageable);

		return contentKbRepository.findByMetadataCategory(category.toLowerCase(), pageable)
				.stream()
				.map(CatalogContentResult::from)
				.toList();
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
                Maximum number of relevant documents to return
                """)
			int limit
	) {
		int effectiveLimit = limit <= 0 ? 5 : Math.min(limit, 10);

		var searchRequest = SearchRequest.builder()
				.query(query)
				.topK(effectiveLimit)
				.build();

		logger.info("Calling similaritySearch with params {}", searchRequest);

		long startedAt = System.nanoTime();
		try {
			return vectorStore.similaritySearch(searchRequest)
					.stream()
					.map(ContentResult::from)
					.toList();
		}
		finally {
			long elapsedMs = (System.nanoTime() - startedAt) / 1_000_000;
			logger.info("similaritySearch completed in {} ms", elapsedMs);
		}
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
		logger.info("Calling countByMetadataCategory with params {}", category);

		return contentKbRepository.countByMetadataCategory(
				category.toLowerCase()
		);
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
		int effectiveLimit = normalizeLimit(limit);
		String today = LocalDate.now().toString();

		var pageable = PageRequest.of(
				0,
				effectiveLimit,
				Sort.by(Sort.Direction.ASC, "metadata.createdAt")
		);

		logger.info("Calling findByMetadataCategoryAndMetadataCreatedAtGreaterThanEqual with params {}, {}", today, pageable);

		return contentKbRepository
				.findUpcomingByCategory(
						"event",
						today,
						pageable
				)
				.stream()
				.map(CatalogContentResult::from)
				.toList();
	}
	private int normalizeLimit(int limit) {
		if (limit <= 0) {
			return DEFAULT_LIMIT;
		}

		return Math.min(limit, MAX_LIMIT);
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
		validatePeriod(startDate, endDate);

		int effectiveLimit = normalizeLimit(limit);

		var pageable = PageRequest.of(
				0,
				effectiveLimit,
				Sort.by(Sort.Direction.DESC, "metadata.createdAt")
		);

		logger.info("Calling findByCategoryAndPeriod with params {}, {}", startDate, endDate);

		return contentKbRepository
				.findByCategoryAndPeriod(
						category.toLowerCase(),
						startDate,
						endDate,
						pageable
				)
				.stream()
				.map(CatalogContentResult::from)
				.toList();
	}

	private void validatePeriod(String startDate, String endDate) {
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);

		if (start.isAfter(end)) {
			throw new IllegalArgumentException(
					"startDate must be before or equal to endDate"
			);
		}
	}
}
