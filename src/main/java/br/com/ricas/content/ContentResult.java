package br.com.ricas.content;

import org.springframework.ai.document.Document;

import java.util.Map;

public record ContentResult(
        String content,
        Map<String, Object> metadata
) {
    public static ContentResult from(Document document) {
        return new ContentResult(
                document.getText(),
                document.getMetadata()
        );
    }
}
