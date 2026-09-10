package br.com.ricas.chat;

import br.com.ricas.plan.PlanStatus;
import br.com.ricas.plan.PlanStep;
import br.com.ricas.plan.StepStatus;
import br.com.ricas.plan.TaskPlan;
import br.com.ricas.plan.TaskPlanRepository;
import br.com.ricas.plan.TaskPlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Proxy;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class ContentChatControllerProgressTest {

    private final AtomicReference<Optional<TaskPlan>> latestPlan =
            new AtomicReference<>(Optional.empty());
    private final AtomicInteger findLatestCalls = new AtomicInteger();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        TaskPlanService taskPlanService = new TaskPlanService(repositoryStub());
        ContentChatController controller = new ContentChatController(
                null,
                null,
                taskPlanService
        );
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    void shouldReturnBadRequestForInvalidConversationId() throws Exception {
        String invalidId = "a".repeat(101);

        mockMvc.perform(get("/chat/progress/{conversationId}", invalidId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));

        org.junit.jupiter.api.Assertions.assertEquals(0, findLatestCalls.get());
    }

    @Test
    void shouldReturnNoContentWhenNoPlanExists() throws Exception {
        mockMvc.perform(get("/chat/progress/conversation-1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        org.junit.jupiter.api.Assertions.assertEquals(1, findLatestCalls.get());
    }

    @Test
    void shouldReturnOnlyPublicProgressFields() throws Exception {
        Instant startedAt = Instant.parse("2026-09-10T10:00:00Z");
        Instant completedAt = Instant.parse("2026-09-10T10:01:00Z");
        PlanStep step = new PlanStep(
                "internal-step-id",
                1,
                "Search relevant content",
                StepStatus.COMPLETED,
                "sensitive internal result",
                List.of("contentSearch"),
                "sensitive internal error",
                startedAt,
                completedAt
        );
        TaskPlan plan = new TaskPlan(
                "internal-plan-id",
                "conversation-1",
                "internal objective",
                PlanStatus.RUNNING,
                List.of(step),
                Instant.parse("2026-09-10T09:59:00Z"),
                completedAt
        );
        latestPlan.set(Optional.of(plan));

        mockMvc.perform(get("/chat/progress/conversation-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RUNNING"))
                .andExpect(jsonPath("$.steps[0].order").value(1))
                .andExpect(jsonPath("$.steps[0].instruction").value("Search relevant content"))
                .andExpect(jsonPath("$.steps[0].status").value("COMPLETED"))
                .andExpect(jsonPath("$.steps[0].toolsUsed[0]").value("contentSearch"))
                .andExpect(jsonPath("$.steps[0].startedAt").value("2026-09-10T10:00:00Z"))
                .andExpect(jsonPath("$.steps[0].completedAt").value("2026-09-10T10:01:00Z"))
                .andExpect(jsonPath("$.steps[0].id").doesNotExist())
                .andExpect(jsonPath("$.steps[0].result").doesNotExist())
                .andExpect(jsonPath("$.steps[0].error").doesNotExist())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.conversationId").doesNotExist())
                .andExpect(jsonPath("$.objective").doesNotExist())
                .andExpect(jsonPath("$.createdAt").doesNotExist())
                .andExpect(jsonPath("$.updatedAt").doesNotExist());
    }

    private TaskPlanRepository repositoryStub() {
        return (TaskPlanRepository) Proxy.newProxyInstance(
                TaskPlanRepository.class.getClassLoader(),
                new Class<?>[]{TaskPlanRepository.class},
                (proxy, method, arguments) -> {
                    if (method.getName().equals(
                            "findFirstByConversationIdOrderByCreatedAtDesc"
                    )) {
                        findLatestCalls.incrementAndGet();
                        return latestPlan.get();
                    }

                    throw new UnsupportedOperationException(method.getName());
                }
        );
    }
}
