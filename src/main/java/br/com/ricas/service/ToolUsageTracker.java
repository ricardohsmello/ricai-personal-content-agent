package br.com.ricas.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class ToolUsageTracker {

	private final ThreadLocal<Set<String>> tools = new ThreadLocal<>();

	public void start() {
		tools.set(new LinkedHashSet<>());
	}

	public void record(String toolName) {
		Set<String> currentTools = tools.get();
		if (currentTools != null) {
			currentTools.add(toolName);
		}
	}

	public List<String> finish() {
		Set<String> currentTools = tools.get();
		tools.remove();
		return currentTools == null
				? List.of()
				: List.copyOf(new ArrayList<>(currentTools));
	}
}
