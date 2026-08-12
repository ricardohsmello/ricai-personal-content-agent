package br.com.ricas.content;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "content_kb")
@CompoundIndex(
		name = "category_createdAt",
		def = "{'metadata.category': 1, 'metadata.createdAt': -1}"
)
public record ContentKb(
		@Id String id,
		String content,
		Metadata metadata
){
	public record Metadata(
			String title,
			String category,
			String createdAt
	) {
	}
}
