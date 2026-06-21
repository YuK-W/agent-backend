package com.yuke.agentbackend.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * LLM 配置类
 * 使用 DeepSeek API (兼容 OpenAI 协议)
 */
@Slf4j
@Configuration
public class LlmConfig {

    @Value("${llm.deepseek.base-url:https://api.deepseek.com}")
    private String baseUrl;

    @Value("${llm.deepseek.api-key}")
    private String apiKey;

    @Value("${llm.deepseek.model-name:deepseek-chat}")
    private String modelName;

    @Value("${llm.deepseek.temperature:0.7}")
    private Double temperature;

    @Value("${llm.deepseek.max-tokens:4096}")
    private Integer maxTokens;

    @Value("${llm.deepseek.timeout:60}")
    private Long timeoutSeconds;

    /**
     * 注册 DeepSeek Chat Model Bean
     * 使用 OpenAI 协议接入 DeepSeek
     */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        log.info("初始化 DeepSeek Chat Model: {}", modelName);
        log.info("API Base URL: {}", baseUrl);

        return OpenAiChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}