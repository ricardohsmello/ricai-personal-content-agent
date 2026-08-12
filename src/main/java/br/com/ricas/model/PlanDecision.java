package br.com.ricas.model;

public record PlanDecision(
		boolean requiresPlan,
		String reason
) {
}
