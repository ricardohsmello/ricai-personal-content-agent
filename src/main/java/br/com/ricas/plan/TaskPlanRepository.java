package br.com.ricas.plan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.Optional;

public interface TaskPlanRepository
        extends MongoRepository<TaskPlan, String> {

    Optional<TaskPlan> findFirstByConversationIdAndStatusInOrderByCreatedAtDesc(
            String conversationId,
            Collection<PlanStatus> statuses
    );
}
