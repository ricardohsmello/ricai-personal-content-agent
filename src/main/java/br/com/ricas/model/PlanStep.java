package br.com.ricas.model;

import java.time.Instant;
import java.util.List;

public record PlanStep(
        String id,
        int order,
        String instruction,
        StepStatus status,
        String result,
        List<String> toolsUsed,
        String error,
        Instant startedAt,
        Instant completedAt
) {
}
