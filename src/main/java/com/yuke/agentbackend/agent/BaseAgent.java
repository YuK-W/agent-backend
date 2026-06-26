package com.yuke.agentbackend.agent;

import com.yuke.agentbackend.llm.LlmProvider;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class BaseAgent implements Agent {

    protected final String name;
    protected final String role;
    protected final String systemPrompt;
    protected final LlmProvider llmProvider;
    protected final List<String> tools;

    public BaseAgent(String name, String role, String systemPrompt, LlmProvider llmProvider) {
        this.name = name;
        this.role = role;
        this.systemPrompt = systemPrompt;
        this.llmProvider = llmProvider;
        this.tools = new ArrayList<>();
    }

    public BaseAgent(String name, String role, String systemPrompt, LlmProvider llmProvider, List<String> tools) {
        this.name = name;
        this.role = role;
        this.systemPrompt = systemPrompt;
        this.llmProvider = llmProvider;
        this.tools = tools != null ? new ArrayList<>(tools) : new ArrayList<>();
    }

    public void addTool(String toolName) {
        this.tools.add(toolName);
    }

    @Override
    public String execute(String input) {
        String fullPrompt = systemPrompt + "\n\n任务: " + input;
        return llmProvider.chat(fullPrompt);
    }

    @Override
    public String execute(String input, String context) {
        String fullPrompt = systemPrompt + "\n\n上下文信息:\n" + context + "\n\n任务: " + input;
        return llmProvider.chat(fullPrompt);
    }
}