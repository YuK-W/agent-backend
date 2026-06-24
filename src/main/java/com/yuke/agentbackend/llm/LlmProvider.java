package com.yuke.agentbackend.llm;

public interface LlmProvider {

    String chat(String message);

    String chat(String systemPrompt, String userMessage);
}