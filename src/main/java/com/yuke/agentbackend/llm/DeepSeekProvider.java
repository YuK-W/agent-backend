package com.yuke.agentbackend.llm;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeepSeekProvider implements LlmProvider {

    private final ChatLanguageModel chatLanguageModel;

    @Override
    public String chat(String message) {
        try {
            log.info("发送消息到 DeepSeek: {}", message);
            String response = chatLanguageModel.generate(message);
            log.info("DeepSeek 回复: {}", response);
            return response;
        } catch (Exception e) {
            log.error("DeepSeek API 调用失败: {}", e.getMessage(), e);
            return "抱歉，我暂时无法回答，请稍后再试。错误: " + e.getMessage();
        }
    }

    @Override
    public String chat(String systemPrompt, String userMessage) {
        try {
            String fullPrompt = systemPrompt + "\n\n用户问题: " + userMessage;
            String response = chatLanguageModel.generate(fullPrompt);
            return response;
        } catch (Exception e) {
            log.error("DeepSeek API 调用失败: {}", e.getMessage(), e);
            return "抱歉，我暂时无法回答，请稍后再试。错误: " + e.getMessage();
        }
    }
}