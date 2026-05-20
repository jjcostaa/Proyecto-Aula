package com.fitzone.fitzonev2.controller;

import com.fitzone.fitzonev2.dto.ChatRequest;
import com.fitzone.fitzonev2.dto.ChatResponse;
import com.fitzone.fitzonev2.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(@RequestBody ChatRequest request) {
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ChatResponse("Por favor escribe un mensaje. 😊", false));
        }

        ChatResponse response = chatbotService.chat(request.getMessage().trim());
        return ResponseEntity.ok(response);
    }
}
