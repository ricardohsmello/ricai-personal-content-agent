package br.com.ricas.plan;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PlanGeneratorService {

    private final ChatClient planChatClient;
    private final TaskPlanService taskPlanService;

    public PlanGeneratorService(
            @Qualifier("planChatClient") ChatClient planChatClient,
            TaskPlanService taskPlanService
    ) {
        this.planChatClient = planChatClient;
        this.taskPlanService = taskPlanService;
    }

    public TaskPlan generate(
            String conversationId,
            String userRequest
    ) {
        PlanProposal proposal = planChatClient.prompt()
                .user(userRequest)
                .call()
                .entity(PlanProposal.class);

        validate(proposal);

        return taskPlanService.create(
                conversationId,
                proposal.objective(),
                proposal.steps()
        );
    }

    private void validate(PlanProposal proposal) {
        if (proposal == null) {
            throw new IllegalStateException(
                    "The model did not generate a plan"
            );
        }

        if (proposal.objective() == null
                || proposal.objective().isBlank()) {
            throw new IllegalStateException(
                    "The generated plan has no objective"
            );
        }

        if (proposal.steps() == null
                || proposal.steps().size() < 2
                || proposal.steps().size() > 5) {
            throw new IllegalStateException(
                    "The plan must contain between 2 and 5 steps"
            );
        }

        if (proposal.steps().stream().anyMatch(
                step -> step == null || step.isBlank()
        )) {
            throw new IllegalStateException(
                    "The plan contains an invalid step"
            );
        }
    }
}
