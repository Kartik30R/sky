package com.askver.sky.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    public String ask(
            String question,
            UUID companyId
    ) {

        SearchRequest request =
                SearchRequest.builder()
                        .query(question)
                        .filterExpression(
                                "companyId == '" + companyId + "'"
                        )
                        .topK(5)
                        .build();

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