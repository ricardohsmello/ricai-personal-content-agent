package br.com.ricas.plan;

import java.util.List;

public record PlanProposal(
        String objective,
        List<String> steps
) {
}
