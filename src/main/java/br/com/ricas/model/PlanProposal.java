package br.com.ricas.model;

import java.util.List;

public record PlanProposal(
        String objective,
        List<String> steps
) {
}