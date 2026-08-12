package br.com.ricas.service;

import br.com.ricas.document.TaskPlan;
import br.com.ricas.model.PlanStatus;
import br.com.ricas.model.StepStatus;
import br.com.ricas.repository.TaskPlanRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskPlanServiceTest {

    private final TaskPlanRepository repository = repositoryStub();

    private final TaskPlanService service =
            new TaskPlanService(repository);

    @Test
    void shouldCreatePlanWithPendingSteps() {
        TaskPlan plan = service.create(
                "conversation-1",
                "Relate events to articles",
                List.of(
                        "Find the most recent events",
                        "Identify the event topics",
                        "Find related articles",
                        "Prepare the response"
                )
        );

        assertEquals(PlanStatus.CREATED, plan.status());
        assertEquals(4, plan.steps().size());
        assertEquals(1, plan.steps().getFirst().order());
        assertEquals(
                StepStatus.PENDING,
                plan.steps().getFirst().status()
        );
    }

    @Test
    void shouldMarkExternalActionAsRequiringConfirmation() {
        TaskPlan plan = service.create(
                "conversation-2",
                "Schedule a meeting",
                List.of(
                        "Find the first available time",
                        "[CONFIRMATION_REQUIRED] Create the scheduling link"
                )
        );

        assertFalse(plan.steps().getFirst().requiresConfirmation());
        assertTrue(plan.steps().get(1).requiresConfirmation());
        assertEquals(
                "Create the scheduling link",
                plan.steps().get(1).instruction()
        );
    }

    private TaskPlanRepository repositoryStub() {
        return (TaskPlanRepository) Proxy.newProxyInstance(
                TaskPlanRepository.class.getClassLoader(),
                new Class<?>[]{TaskPlanRepository.class},
                (proxy, method, arguments) -> {
                    if (method.getName().equals("save")) {
                        return arguments[0];
                    }

                    throw new UnsupportedOperationException(method.getName());
                }
        );
    }
}
