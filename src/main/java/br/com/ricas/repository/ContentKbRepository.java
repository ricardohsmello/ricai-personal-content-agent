package br.com.ricas.repository;

import br.com.ricas.model.ContentKb;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentKbRepository extends MongoRepository<ContentKb, String> {

	List<ContentKb> findByMetadataCategory(String category, Pageable pageable);

	long countByMetadataCategory(String category);

	List<ContentKb> findByMetadataCategoryAndMetadataCreatedAtGreaterThanEqual(
			String category,
			String createdAt,
			Pageable pageable
	);

	@Query("""
    {
      'metadata.category': ?0,
      'metadata.createdAt': {
        '$gte': ?1,
        '$lte': ?2
      }
    }
    """)
	List<ContentKb> findByCategoryAndPeriod(
			String category,
			String startDate,
			String endDate,
			Pageable pageable
	);
}
