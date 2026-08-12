package br.com.ricas.plan;

public record PlanDecision(
		boolean requiresPlan,
		String reason
) {
}
