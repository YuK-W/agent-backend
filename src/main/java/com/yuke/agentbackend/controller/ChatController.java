package com.yuke.agentbackend.controller;

import com.yuke.agentbackend.llm.LlmProvider;
import com.yuke.agentbackend.model.ChatRequest;
import com.yuke.agentbackend.model.ChatResponse;
import com.yuke.agentbackend.model.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final LlmProvider llmProvider;

    @PostMapping("/chat")
    public CommonResponse<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("收到对话请求: {}", request.getMessage());

        String content = llmProvider.chat(request.getMessage());

        ChatResponse response = ChatResponse.builder()
                .content(content)
                .timestamp(System.currentTimeMillis())
                .build();

        return CommonResponse.success(response);
    }
}