package br.com.ricas.service;

import br.com.ricas.model.CatalogContentResult;
import br.com.ricas.model.ContentResult;
import br.com.ricas.repository.ContentKbRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class KnowledgeBaseService {

	private static final Logger logger = LoggerFactory.getLogger(KnowledgeBaseService.class);
	private final ContentKbRepository contentKbRepository;
	private final VectorStore vectorStore;

	private static final int DEFAULT_LIMIT = 5;
	private static final int MAX_LIMIT = 10;
	private static final int SEMANTIC_LIMIT = 3;

	KnowledgeBaseService(
			ContentKbRepository contentKbRepository,
			VectorStore vectorStore
	) {
		this.contentKbRepository = contentKbRepository;
		this.vectorStore = vectorStore;
	}

	public List<CatalogContentResult> findByMetadataCategory(String category, int limit) {
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

	public List<ContentResult> searchKnowledgeBase(String query, int limit) {
		int effectiveLimit = limit <= 0 ? SEMANTIC_LIMIT : Math.min(limit, SEMANTIC_LIMIT);

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

	public long countByMetadataCategory(String category) {
		logger.info("Calling countByMetadataCategory with params {}", category);

		return contentKbRepository.countByMetadataCategory(
				category.toLowerCase()
		);
	}

	public List<CatalogContentResult> findUpcomingEvents(int limit) {
		int effectiveLimit = normalizeLimit(limit);
		String today = LocalDate.now().toString();

		var pageable = PageRequest.of(
				0,
				effectiveLimit,
				Sort.by(Sort.Direction.ASC, "metadata.createdAt")
		);

		logger.info("Calling findUpcomingByCategory with params {}, {}", today, pageable);

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

	public List<CatalogContentResult> findContentByPeriod(String category, String startDate, String endDate, int limit) {
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
