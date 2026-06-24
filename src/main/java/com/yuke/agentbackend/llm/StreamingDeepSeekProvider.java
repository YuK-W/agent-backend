package com.yuke.agentbackend.llm;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamingDeepSeekProvider {

    private final ChatLanguageModel chatLanguageModel;

    /**
     * 模拟流式对话（先获取完整回答，再逐字输出）
     * 体验上等同于流式，但不依赖 LangChain4j 的流式 API
     */
    public void chatStream(String message, Consumer<String> onNext, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            log.info("开始对话: {}", message);

            // 1. 调用 DeepSeek 获取完整回答（非流式）
            String fullResponse = chatLanguageModel.generate(message);
            log.info("获取完整回答，长度: {} 字符", fullResponse.length());

            // 2. 模拟流式：逐字输出（每 30ms 输出一个字符）
            for (char c : fullResponse.toCharArray()) {
                onNext.accept(String.valueOf(c));
                try {
                    Thread.sleep(30); // 模拟打字效果，可调整
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("流式输出被中断");
                    break;
                }
            }

            // 3. 完成
            onComplete.run();

        } catch (Exception e) {
            log.error("对话出错", e);
            onError.accept(e);
        }
    }
}