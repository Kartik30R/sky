package com.askver.sky.controller;

import com.askver.sky.DTO.ChatRequest;
import com.askver.sky.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

    @PostMapping
    public String ask(@RequestBody ChatRequest request) {
        return service.ask(request.getMessage());
    }
}