package br.com.ricas.tools;

import br.com.ricas.model.AvailableMeetingTime;
import br.com.ricas.model.SchedulingLinkResult;
import br.com.ricas.service.CalendlyService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulingTools {

	private final CalendlyService calendlyService;

	SchedulingTools(CalendlyService calendlyService) {
		this.calendlyService = calendlyService;
	}

	@Tool(description = """
			Lists available times for a meeting with Ricardo. This is a read-only
			operation. Use it before asking the user to select and confirm a time.
			""")
	public List<AvailableMeetingTime> findAvailableMeetingTimes(
			@ToolParam(description = "Future range start as an ISO-8601 UTC instant") String startTime,
			@ToolParam(description = "Range end as an ISO-8601 UTC instant, at most 31 days after start") String endTime,
			@ToolParam(description = "Maximum number of times to return; use 5 when unspecified") int limit
	) {
		return calendlyService.findAvailableTimes(startTime, endTime, limit);
	}

	@Tool(description = """
			Creates a public Calendly scheduling link prefilled with the invitee's
			name, email, and a brief description of what they want to discuss. Ask
			for any missing information before using this capability. This does not
			create a meeting; the user must open the link, select an available time,
			and confirm the booking in Calendly.
			""")
	public SchedulingLinkResult createSchedulingLink(
			@ToolParam(description = "Invitee's full name") String name,
			@ToolParam(description = "Invitee's email address") String email,
			@ToolParam(description = "Brief description of what the invitee wants to discuss")
			String meetingDescription
	) {
		return calendlyService.createSchedulingLink(name, email, meetingDescription);
	}
}
