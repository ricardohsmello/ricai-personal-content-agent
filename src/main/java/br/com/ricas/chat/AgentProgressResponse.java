package br.com.ricas.chat;

import br.com.ricas.plan.PlanStatus;
import br.com.ricas.plan.StepStatus;
import br.com.ricas.plan.TaskPlan;

import java.time.Instant;
import java.util.List;

public record AgentProgressResponse(
        PlanStatus status,
        List<StepProgress> steps
) {
    static AgentProgressResponse from(TaskPlan plan) {
        return new AgentProgressResponse(
                plan.status(),
                plan.steps().stream()
                        .map(step -> new StepProgress(
                                step.order(),
                                step.instruction(),
                                step.status(),
                                step.toolsUsed(),
                                step.startedAt(),
                                step.completedAt()
                        ))
                        .toList()
        );
    }

    public record StepProgress(
            int order,
            String instruction,
            StepStatus status,
            List<String> toolsUsed,
            Instant startedAt,
            Instant completedAt
    ) {
    }
}
