package com.yuke.agentbackend.controller;

import com.yuke.agentbackend.llm.StreamingDeepSeekProvider;
import com.yuke.agentbackend.model.ChatRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StreamingController {

    private final StreamingDeepSeekProvider streamingProvider;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatStream(@Valid @RequestBody ChatRequest request) {
        SseEmitter emitter = new SseEmitter(120000L);

        executor.execute(() -> {
            try {
                streamingProvider.chatStream(
                        request.getMessage(),
                        token -> {
                            try {
                                emitter.send(SseEmitter.event()
                                        .name("message")
                                        .data(token));
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        error -> {
                            try {
                                emitter.send(SseEmitter.event()
                                        .name("error")
                                        .data("出错: " + error.getMessage()));
                                emitter.complete();
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        () -> {
                            try {
                                emitter.send(SseEmitter.event()
                                        .name("complete")
                                        .data("[DONE]"));
                                emitter.complete();
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        }
                );
            } catch (Exception e) {
                log.error("流式请求失败", e);
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}