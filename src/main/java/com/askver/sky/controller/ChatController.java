package com.askver.sky.controller;

import com.askver.sky.DTO.ChatRequest;
import com.askver.sky.model.Role;
import com.askver.sky.service.ChatService;
import com.askver.sky.userdetail.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

  
    @PostMapping("/{department}")
    public ResponseEntity<String> ask(
            @PathVariable String department,
            @RequestBody ChatRequest request,
            Authentication authentication
    ) {
        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ROLE_ADMIN.name()));

        if (!isAdmin) {
      
            String userDept = user.getAuthorities().stream()
                    .findFirst()
                    .map(a -> a.getAuthority()
                            .replace("ROLE_", "")
                            .toLowerCase())
                    .orElse("");

            if (!userDept.equals(department.toLowerCase())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access denied: you can only access the /" + userDept + " chatbot.");
            }
        }

        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest().body("Please provide a question.");
        }

        String answer = service.ask(
                request.getMessage(),
                user.getCompanyId(),
                user.getUserId(),
                department
        );

        return ResponseEntity.ok(answer);
    }
}
