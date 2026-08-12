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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class KnowledgeBaseService {

	private static final Logger logger = LoggerFactory.getLogger(KnowledgeBaseService.class);
	private final ContentKbRepository contentKbRepository;
	private final VectorStore vectorStore;
	private final MongoTemplate mongoTemplate;

	private static final int DEFAULT_LIMIT = 5;
	private static final int MAX_LIMIT = 10;
	private static final int SEMANTIC_LIMIT = 3;
	private static final int EXPERIENCE_LIMIT = 100;

	KnowledgeBaseService(
			ContentKbRepository contentKbRepository,
			VectorStore vectorStore,
			MongoTemplate mongoTemplate
	) {
		this.contentKbRepository = contentKbRepository;
		this.vectorStore = vectorStore;
		this.mongoTemplate = mongoTemplate;
	}

	public List<CatalogContentResult> findByMetadataCategory(String category, int limit) {
		return findByCategoryOrderedByDate(category, Sort.Direction.DESC, limit);
	}

	public List<CatalogContentResult> findProfessionalExperience() {
		var pageable = PageRequest.of(
				0,
				EXPERIENCE_LIMIT,
				Sort.by(Sort.Direction.DESC, "metadata.createdAt")
		);

		logger.info("Calling findProfessionalExperience with params {}", pageable);

		return contentKbRepository.findByMetadataCategory("experience", pageable)
				.stream()
				.map(CatalogContentResult::from)
				.toList();
	}

	public List<CatalogContentResult> findByCategoryOrderedByDate(
			String category,
			Sort.Direction direction,
			int limit
	) {
		int effectiveLimit = limit <= 0 ? DEFAULT_LIMIT : Math.min(limit, MAX_LIMIT);
		var pageable = PageRequest.of(
				0,
				effectiveLimit,
				Sort.by(direction, "metadata.createdAt")
		);

		logger.info("Calling findByMetadataCategory with params {}, {}", category, pageable);

		return contentKbRepository.findByMetadataCategory(category.toLowerCase(), pageable)
				.stream()
				.map(CatalogContentResult::from)
				.toList();
	}

	public List<CatalogContentResult> findByTopicsOrderedByDate(
			String category,
			List<String> topics,
			Sort.Direction direction,
			int limit
	) {
		if (topics == null || topics.isEmpty()) {
			throw new IllegalArgumentException("At least one topic is required");
		}

		Criteria[] topicCriteria = topics.stream()
				.filter(topic -> topic != null && !topic.isBlank())
				.map(String::trim)
				.map(topic -> "(?<![\\p{L}\\p{N}_])"
						+ Pattern.quote(topic)
						+ "(?![\\p{L}\\p{N}_])")
				.map(topic -> Criteria.where("content").regex(topic, "i"))
				.toArray(Criteria[]::new);

		if (topicCriteria.length == 0) {
			throw new IllegalArgumentException("At least one valid topic is required");
		}

		Query query = new Query()
				.addCriteria(Criteria.where("metadata.category").is(category.toLowerCase()))
				.addCriteria(new Criteria().orOperator(topicCriteria))
				.with(Sort.by(direction, "metadata.createdAt"))
				.limit(normalizeLimit(limit));

		query.fields().include("content").include("metadata");

		logger.info(
				"Calling findByTopicsOrderedByDate with category {}, topics {}, direction {}",
				category,
				topics,
				direction
		);

		return mongoTemplate.find(query, br.com.ricas.document.ContentKb.class)
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
		String today = todayInSaoPaulo();

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

	public List<CatalogContentResult> findLatestPastEvents(int limit) {
		int effectiveLimit = normalizeLimit(limit);
		String today = todayInSaoPaulo();

		var pageable = PageRequest.of(
				0,
				effectiveLimit,
				Sort.by(Sort.Direction.DESC, "metadata.createdAt")
		);

		logger.info("Calling findPastByCategory with params {}, {}", today, pageable);

		return contentKbRepository
				.findPastByCategory("event", today, pageable)
				.stream()
				.map(CatalogContentResult::from)
				.toList();
	}

	private String todayInSaoPaulo() {
		return LocalDate.now(ZoneId.of("America/Sao_Paulo")).toString();
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
