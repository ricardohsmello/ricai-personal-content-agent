package br.com.ricas.tools;

import br.com.ricas.model.AvailableMeetingTime;
import br.com.ricas.service.CalendlyService;
import br.com.ricas.service.ToolUsageTracker;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulingReadTools {

	private final CalendlyService calendlyService;
	private final ToolUsageTracker toolUsageTracker;

	public SchedulingReadTools(
			CalendlyService calendlyService,
			ToolUsageTracker toolUsageTracker
	) {
		this.calendlyService = calendlyService;
		this.toolUsageTracker = toolUsageTracker;
	}

	@Tool(description = """
			Lists available times for a meeting with Ricardo. This operation is
			read-only. Preserve the selected startTime, including its UTC offset,
			because a later scheduling step may need it.
			""")
	public List<AvailableMeetingTime> findAvailableMeetingTimes(
			@ToolParam(description = "Future range start as an ISO-8601 UTC instant") String startTime,
			@ToolParam(description = "Range end as an ISO-8601 UTC instant, at most 31 days after start") String endTime,
			@ToolParam(description = "Maximum number of times; use 1 for the first available time") int limit
	) {
		toolUsageTracker.record("findAvailableMeetingTimes");
		return calendlyService.findAvailableTimes(startTime, endTime, limit);
	}

	@Tool(description = """
			Lists Ricardo's next available meeting times starting from the actual
			current instant calculated by the server. Use when no exact date range
			was specified. Do not call getDate or ask for the current date.
			""")
	public List<AvailableMeetingTime> findNextAvailableMeetingTimes(
			@ToolParam(description = "Future days to search; use 31 when unspecified, maximum 31") int daysAhead,
			@ToolParam(description = "Maximum results; use 1 for the first available time") int limit
	) {
		toolUsageTracker.record("findNextAvailableMeetingTimes");
		return calendlyService.findNextAvailableTimes(daysAhead, limit);
	}
}
