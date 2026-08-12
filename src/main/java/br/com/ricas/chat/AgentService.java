package br.com.ricas.chat;

import br.com.ricas.plan.PlanExecutorService;
import br.com.ricas.plan.PlanGeneratorService;
import br.com.ricas.plan.PlanRoutingService;
import br.com.ricas.plan.PlanStep;
import br.com.ricas.plan.StepStatus;
import br.com.ricas.plan.TaskPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class AgentService {

	private final Logger logger = LoggerFactory.getLogger(AgentService.class);
	private final PlanRoutingService planRoutingService;
	private final PlanGeneratorService planGeneratorService;
	private final PlanExecutorService planExecutorService;
	private final ChatService chatService;

	public AgentService(
			PlanRoutingService planRoutingService,
			PlanGeneratorService planGeneratorService,
			PlanExecutorService planExecutorService,
			ChatService chatService
	) {
		this.planRoutingService = planRoutingService;
		this.planGeneratorService = planGeneratorService;
		this.planExecutorService = planExecutorService;
		this.chatService = chatService;
	}

	public String respond(ChatRequest request) {
		if (!planRoutingService.requiresPlan(request.message())) {
			return chatService.chat(request);
		}

		logger.info("A new plan was required.");

		TaskPlan createdPlan = planGeneratorService.generate(
				request.conversationId(),
				request.message()
		);

		logger.info("Plan {} was generated.", createdPlan);

		TaskPlan completedPlan = planExecutorService.executeAll(createdPlan.id());
		String answer = finalAnswer(completedPlan);
		chatService.recordExchange(request, answer);

		return answer;
	}

	private String finalAnswer(TaskPlan plan) {
		return plan.steps().stream()
				.filter(step -> step.status() == StepStatus.COMPLETED)
				.filter(step -> step.result() != null && !step.result().isBlank())
				.max(Comparator.comparingInt(PlanStep::order))
				.map(PlanStep::result)
				.orElseThrow(() -> new IllegalStateException(
						"The plan completed without a final answer"
				));
	}
}
