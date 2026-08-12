package br.com.ricas.tools;

import br.com.ricas.model.AvailableMeetingTime;
import br.com.ricas.model.SchedulingLinkResult;
import br.com.ricas.service.CalendlyService;
import br.com.ricas.service.ToolUsageTracker;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulingTools {

	private final CalendlyService calendlyService;
	private final ToolUsageTracker toolUsageTracker;

	SchedulingTools(CalendlyService calendlyService, ToolUsageTracker toolUsageTracker) {
		this.calendlyService = calendlyService;
		this.toolUsageTracker = toolUsageTracker;
	}

	@Tool(description = """
			Lists available times for a meeting with Ricardo. This is a read-only
			operation. Returned start times are already converted to the
			America/Sao_Paulo timezone and must be presented without converting
			them again. Use it when the user needs to select a time.
			""")
	public List<AvailableMeetingTime> findAvailableMeetingTimes(
			@ToolParam(description = "Future range start as an ISO-8601 UTC instant") String startTime,
			@ToolParam(description = "Range end as an ISO-8601 UTC instant, at most 31 days after start") String endTime,
			@ToolParam(description = "Maximum number of times to return; use 5 when unspecified") int limit
	) {
		toolUsageTracker.record("findAvailableMeetingTimes");
		return calendlyService.findAvailableTimes(startTime, endTime, limit);
	}

	@Tool(description = """
			Lists Ricardo's next available meeting times starting from the actual
			current instant calculated by the server. Use this capability when the
			user asks for available times without specifying an exact date range.
			Do not call getDate and do not ask the user for the current date.
			""")
	public List<AvailableMeetingTime> findNextAvailableMeetingTimes(
			@ToolParam(description = "Number of future days to search; use 31 when unspecified, maximum 31")
			int daysAhead,
			@ToolParam(description = "Maximum number of times; use 5 when unspecified")
			int limit
	) {
		toolUsageTracker.record("findNextAvailableMeetingTimes");
		return calendlyService.findNextAvailableTimes(daysAhead, limit);
	}

	@Tool(description = """
			Creates a public Calendly scheduling link prefilled with the invitee's
			name, email, meeting description, and selected start time. The capability
			rechecks Calendly availability and resolves the scheduling URL internally,
			so the user never needs to provide or preserve a schedulingUrl. Ask for
			missing or ambiguous information before using it. This does not create a
			meeting; the user must open the link and finish the booking in Calendly.
			Present the URL as a short, descriptive Markdown link.
			""")
	public SchedulingLinkResult createSchedulingLink(
			@ToolParam(description = "Invitee's full name") String name,
			@ToolParam(description = "Invitee's email address") String email,
			@ToolParam(description = "Brief description of what the invitee wants to discuss")
			String meetingDescription,
			@ToolParam(description = "Selected available start time exactly as returned by findAvailableMeetingTimes, including UTC offset")
			String selectedStartTime
	) {
		toolUsageTracker.record("createSchedulingLink");
		return calendlyService.createSchedulingLinkForTime(
				name,
				email,
				meetingDescription,
				selectedStartTime
		);
	}
}
