package br.com.ricas.service;

import br.com.ricas.document.TaskPlan;
import br.com.ricas.model.PlanStatus;
import br.com.ricas.model.PlanStep;
import br.com.ricas.model.StepStatus;
import br.com.ricas.repository.TaskPlanRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class TaskPlanService {

    private static final List<PlanStatus> ACTIVE_STATUSES = List.of(
            PlanStatus.CREATED,
            PlanStatus.RUNNING,
            PlanStatus.WAITING_USER
    );

    private final TaskPlanRepository repository;

    public TaskPlanService(TaskPlanRepository repository) {
        this.repository = repository;
    }

    public TaskPlan create(
            String conversationId,
            String objective,
            List<String> instructions
    ) {
        if (instructions == null || instructions.isEmpty()) {
            throw new IllegalArgumentException(
                    "A plan must contain at least one step"
            );
        }

        Instant now = Instant.now();

        TaskPlan plan = new TaskPlan(
                null,
                conversationId,
                objective,
                PlanStatus.CREATED,
                createSteps(instructions),
                now,
                now
        );

        return repository.save(plan);
    }

    public Optional<TaskPlan> findActive(String conversationId) {
        return repository
                .findFirstByConversationIdAndStatusInOrderByCreatedAtDesc(
                        conversationId,
                        ACTIVE_STATUSES
                );
    }

    public TaskPlan startNextStep(String planId) {
        TaskPlan plan = findById(planId);

        if (plan.status() == PlanStatus.COMPLETED) {
            return plan;
        }

        if (plan.status() == PlanStatus.FAILED) {
            throw new IllegalStateException(
                    "A failed plan cannot continue"
            );
        }

        Optional<PlanStep> runningStep = plan.steps().stream()
                .filter(step -> step.status() == StepStatus.RUNNING)
                .findFirst();

        if (runningStep.isPresent()) {
            return plan;
        }

        Optional<PlanStep> nextStep = plan.steps().stream()
                .filter(step -> step.status() == StepStatus.PENDING)
                .min(java.util.Comparator.comparingInt(PlanStep::order));

        if (nextStep.isEmpty()) {
            return saveWithStatus(plan, PlanStatus.COMPLETED);
        }

        Instant now = Instant.now();
        String stepId = nextStep.get().id();

        List<PlanStep> updatedSteps = plan.steps().stream()
                .map(step -> step.id().equals(stepId)
                        ? new PlanStep(
                        step.id(),
                        step.order(),
                        step.instruction(),
                        StepStatus.RUNNING,
                        step.result(),
                        step.toolsUsed(),
                        null,
                        now,
                        null
                )
                        : step)
                .toList();

        return save(plan, PlanStatus.RUNNING, updatedSteps);
    }

    public TaskPlan completeStep(
            String planId,
            String stepId,
            String result,
            List<String> toolsUsed
    ) {
        TaskPlan plan = findById(planId);
        Instant now = Instant.now();

        List<PlanStep> updatedSteps = plan.steps().stream()
                .map(step -> step.id().equals(stepId)
                        ? complete(step, result, toolsUsed, now)
                        : step)
                .toList();

        boolean hasRemainingSteps = updatedSteps.stream()
                .anyMatch(step ->
                        step.status() == StepStatus.PENDING
                                || step.status() == StepStatus.RUNNING
                );

        PlanStatus status = hasRemainingSteps
                ? PlanStatus.RUNNING
                : PlanStatus.COMPLETED;

        return save(plan, status, updatedSteps);
    }

    public TaskPlan failStep(
            String planId,
            String stepId,
            String error,
            List<String> toolsUsed
    ) {
        TaskPlan plan = findById(planId);
        Instant now = Instant.now();

        List<PlanStep> updatedSteps = plan.steps().stream()
                .map(step -> step.id().equals(stepId)
                        ? fail(step, error, toolsUsed, now)
                        : step)
                .toList();

        return save(plan, PlanStatus.FAILED, updatedSteps);
    }

    private PlanStep complete(
            PlanStep step,
            String result,
            List<String> toolsUsed,
            Instant completedAt
    ) {
        if (step.status() != StepStatus.RUNNING) {
            throw new IllegalStateException(
                    "Only a running step can be completed"
            );
        }

        return new PlanStep(
                step.id(),
                step.order(),
                step.instruction(),
                StepStatus.COMPLETED,
                result,
                List.copyOf(toolsUsed),
                null,
                step.startedAt(),
                completedAt
        );
    }

    private PlanStep fail(
            PlanStep step,
            String error,
            List<String> toolsUsed,
            Instant completedAt
    ) {
        if (step.status() != StepStatus.RUNNING) {
            throw new IllegalStateException(
                    "Only a running step can fail"
            );
        }

        return new PlanStep(
                step.id(),
                step.order(),
                step.instruction(),
                StepStatus.FAILED,
                null,
                List.copyOf(toolsUsed),
                error,
                step.startedAt(),
                completedAt
        );
    }

    private TaskPlan findById(String planId) {
        return repository.findById(planId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Plan not found")
                );
    }

    private TaskPlan saveWithStatus(
            TaskPlan plan,
            PlanStatus status
    ) {
        return save(plan, status, plan.steps());
    }

    private TaskPlan save(
            TaskPlan plan,
            PlanStatus status,
            List<PlanStep> steps
    ) {
        return repository.save(new TaskPlan(
                plan.id(),
                plan.conversationId(),
                plan.objective(),
                status,
                steps,
                plan.createdAt(),
                Instant.now()
        ));
    }

    private List<PlanStep> createSteps(List<String> instructions) {
        return IntStream.range(0, instructions.size())
                .mapToObj(index -> new PlanStep(
                        UUID.randomUUID().toString(),
                        index + 1,
                        instructions.get(index),
                    StepStatus.PENDING,
                    null,
                    List.of(),
                    null,
                        null,
                        null
                ))
                .toList();
    }
}
