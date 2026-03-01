package com.askver.sky.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String ask(String question) {

        return chatClient
                .prompt()
                .system("""
You are an AI assistant helping citizens
understand government schemes.
Answer strictly using retrieved documents.
If not found, say you don't know.
""")
                .user(question)
                .call()
                .content();
    }
}