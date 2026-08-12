package br.com.ricas.chat;

public record ChatRequest(
			String message,
			String conversationId
	) {	}
