package br.com.ricas.plan;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PlanRoutingService {

	private final ChatClient planRouterChatClient;

	public PlanRoutingService(
			@Qualifier("planRouterChatClient") ChatClient planRouterChatClient
	) {
		this.planRouterChatClient = planRouterChatClient;
	}

	public boolean requiresPlan(String userRequest) {
		PlanDecision decision = planRouterChatClient.prompt()
				.user(userRequest)
				.call()
				.entity(PlanDecision.class);

		return decision != null && decision.requiresPlan();
	}
}
