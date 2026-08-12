package br.com.ricas.service;

import br.com.ricas.document.TaskPlan;
import br.com.ricas.model.PlanStatus;
import br.com.ricas.model.PlanStep;
import br.com.ricas.model.StepStatus;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PlanExecutorService {

    private final ChatClient executorChatClient;
    private final TaskPlanService taskPlanService;
    private final ToolUsageTracker toolUsageTracker;
    private static final int MAX_EXECUTION_STEPS = 5;
    public PlanExecutorService(
            @Qualifier("planExecutorChatClient")
            ChatClient executorChatClient,
            TaskPlanService taskPlanService,
            ToolUsageTracker toolUsageTracker
    ) {
        this.executorChatClient = executorChatClient;
        this.taskPlanService = taskPlanService;
        this.toolUsageTracker = toolUsageTracker;
    }

    public TaskPlan executeAll(String planId) {
        TaskPlan plan = null;

        for (int attempt = 0;
             attempt < MAX_EXECUTION_STEPS;
             attempt++) {

            plan = executeNext(planId);

            if (plan.status() == PlanStatus.COMPLETED
                    || plan.status() == PlanStatus.FAILED
                    || plan.status() == PlanStatus.WAITING_USER) {
                return plan;
            }
        }

        throw new IllegalStateException(
                "Plan exceeded the maximum number of execution steps"
        );
    }

    public TaskPlan executeNext(String planId) {
        TaskPlan plan = taskPlanService.startNextStep(planId);

        if (plan.status() == PlanStatus.COMPLETED) {
            return plan;
        }

        PlanStep currentStep = plan.steps().stream()
                .filter(step -> step.status() == StepStatus.RUNNING)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("No running step found")
                );

        toolUsageTracker.start();
        try {
            String result = executorChatClient.prompt()
                    .user(buildExecutionPrompt(plan, currentStep))
                    .call()
                    .content();

            return taskPlanService.completeStep(
                    plan.id(),
                    currentStep.id(),
                    result,
                    toolUsageTracker.finish()
            );
        }
        catch (Exception exception) {
            var toolsUsed = toolUsageTracker.finish();
            String error = exception.getMessage() != null
                    ? exception.getMessage()
                    : exception.getClass().getSimpleName();

            taskPlanService.failStep(
                    plan.id(),
                    currentStep.id(),
                    error,
                    toolsUsed
            );

            throw exception;
        }
    }

    private String buildExecutionPrompt(
            TaskPlan plan,
            PlanStep currentStep
    ) {
        return """
                PLAN OBJECTIVE:
                %s

                PREVIOUS STEP RESULTS:
                %s

                CURRENT STEP:
                %s
                """.formatted(
                plan.objective(),
                previousResults(plan),
                currentStep.instruction()
        );
    }

    private String previousResults(TaskPlan plan) {
        String results = plan.steps().stream()
                .filter(step ->
                        step.status() == StepStatus.COMPLETED
                                && step.result() != null
                )
                .map(step -> """
                        Step %d: %s
                        Result: %s
                        """.formatted(
                        step.order(),
                        step.instruction(),
                        step.result()
                ))
                .reduce("", (first, second) -> first + "\n" + second);

        return results.isBlank()
                ? "No previous results."
                : results;
    }
}
