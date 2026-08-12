package br.com.ricas.document;

import br.com.ricas.model.PlanStatus;
import br.com.ricas.model.PlanStep;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document("task_plans")
@CompoundIndex(
        name = "conversation_status_createdAt",
        def = "{'conversationId': 1, 'status': 1, 'createdAt': -1}"
)
public record TaskPlan(
        @Id
        String id,

        String conversationId,
        String objective,
        PlanStatus status,
        boolean confirmationGranted,
        List<PlanStep> steps,
        Instant createdAt,
        Instant updatedAt
) {
}
