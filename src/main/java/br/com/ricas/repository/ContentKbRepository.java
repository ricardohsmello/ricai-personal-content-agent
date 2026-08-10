package br.com.ricas.repository;

import br.com.ricas.model.ContentKb;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentKbRepository extends MongoRepository<ContentKb, String> {

	@Query(
			value = "{'metadata.category': ?0}",
			fields = "{'content': 1, 'metadata': 1}"
	)
	List<ContentKb> findByMetadataCategory(String category, Pageable pageable);

	long countByMetadataCategory(String category);

	@Query(
			value = "{'metadata.category': ?0, 'metadata.createdAt': {'$gte': ?1}}",
			fields = "{'content': 1, 'metadata': 1}"
	)
	List<ContentKb> findUpcomingByCategory(
			String category,
			String createdAt,
			Pageable pageable
	);

	@Query(
			value = """
					{
					  'metadata.category': ?0,
					  'metadata.createdAt': {
					    '$gte': ?1,
					    '$lte': ?2
					  }
					}
					""",
			fields = "{'content': 1, 'metadata': 1}"
	)
	List<ContentKb> findByCategoryAndPeriod(
			String category,
			String startDate,
			String endDate,
			Pageable pageable
	);
}
