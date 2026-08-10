package br.com.ricas.web;

import br.com.ricas.model.ChatRequest;
import br.com.ricas.service.ContentKbService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ContentChatController {

	private final ContentKbService contentKbService;

	ContentChatController(ContentKbService contentKbService) {
		this.contentKbService = contentKbService;
	}

	@PostMapping
	public String chat(@RequestBody ChatRequest chatRequest) {
		return contentKbService.chat(chatRequest);
	}

}
