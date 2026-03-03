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
            @RequestBody ChatRequest req,
            Authentication authentication
    ) {

        CustomUserDetails user =
                (CustomUserDetails)
                        authentication.getPrincipal();

        return service.ask(
                req.getMessage(),
                user.getCompanyId()
        );
    }}