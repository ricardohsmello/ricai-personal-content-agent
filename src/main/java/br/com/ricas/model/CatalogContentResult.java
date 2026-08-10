package br.com.ricas.model;

import java.util.List;
import java.util.regex.Pattern;

public record CatalogContentResult(
		String title,
		String category,
		String date,
		String summary,
		List<String> links
) {
	private static final int MAX_SUMMARY_LENGTH = 400;
	private static final Pattern URL_PATTERN = Pattern.compile("https?://\\S+");

	public static CatalogContentResult from(ContentKb contentKb) {
		String content = contentKb.content() == null ? "" : contentKb.content();
		var matcher = URL_PATTERN.matcher(content);
		var links = matcher.results()
				.map(result -> removeTrailingPunctuation(result.group()))
				.distinct()
				.toList();

		String summary = URL_PATTERN.matcher(content)
				.replaceAll("")
				.replaceAll("\\s+", " ")
				.trim();

		if (summary.length() > MAX_SUMMARY_LENGTH) {
			summary = summary.substring(0, MAX_SUMMARY_LENGTH).stripTrailing() + "...";
		}

		var metadata = contentKb.metadata();
		return new CatalogContentResult(
				metadata.title(),
				metadata.category(),
				metadata.createdAt(),
				summary,
				links
		);
	}

	private static String removeTrailingPunctuation(String value) {
		int end = value.length();
		while (end > 0 && ".,;:!?)]}".indexOf(value.charAt(end - 1)) >= 0) {
			end--;
		}
		return value.substring(0, end);
	}
}
