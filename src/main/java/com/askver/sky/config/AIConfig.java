package com.askver.sky.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    ChatClient chatClient(
            ChatClient.Builder builder,
            VectorStore vectorStore
    ) {

        return builder
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(vectorStore)
                )
                .build();
    }
}