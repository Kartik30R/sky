package com.askver.sky.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
 import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;

     public ChatService(ChatClient.Builder builder, VectorStore vectorStore, ChatMemory chatMemory) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
        this.chatMemory = chatMemory;
    }

    public String ask(String question, UUID companyId, UUID userId) {

         SearchRequest request = SearchRequest.builder()
                .query(question)
                .filterExpression("companyId == '" + companyId + "'")
                .topK(5)
                .build();

        return chatClient
                .prompt()
                .system("""
                    You are an AI assistant helping company employees understand documents.
                    Answer strictly using retrieved documents. If not found, say you don't know.
                    """)
                .advisors(
                         new QuestionAnswerAdvisor(vectorStore, request),
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .advisors(advisor -> advisor
                         .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, userId.toString())
                )
                .user(question)
                .call()
                .content();
    }
}