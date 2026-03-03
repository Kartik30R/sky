package com.askver.sky.controller;

import com.askver.sky.DTO.ChatRequest;
import com.askver.sky.service.ChatService;
import com.askver.sky.userdetail.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

    @PostMapping
    public String ask(
            @RequestBody ChatRequest request,
            Authentication authentication
    ) {

        CustomUserDetails user =
                (CustomUserDetails)
                        authentication.getPrincipal();
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return "Please provide a question.";
        }
        return service.ask(
                request.getMessage(),
                user.getCompanyId(),
                user.getUserId()
        );
    }}