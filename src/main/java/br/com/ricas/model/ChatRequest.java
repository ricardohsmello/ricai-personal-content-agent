package br.com.ricas.model;

public record ChatRequest(
			String message,
			String conversationId
	) {	}