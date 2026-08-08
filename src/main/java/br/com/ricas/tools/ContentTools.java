package br.com.ricas.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ContentTools {

	@Tool(description = "Get the current date")
	public Date getDate() {
		return new Date();
	}
}
