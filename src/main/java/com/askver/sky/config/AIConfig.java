package com.askver.sky.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    ChatClient chatClient(
            ChatClient.Builder builder,
            VectorStore vectorStore,
            ChatMemory chatMemory
    ) {

        return builder
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(vectorStore),
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .build();
    }
}