package com.yuke.agentbackend.rag;

import dev.langchain4j.data.segment.TextSegment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Retriever {

    private final VectorStoreService vectorStoreService;

    @Value("${rag.top-k:4}")
    private int defaultTopK;

    /**
     * 检索相关文档片段
     */
    public List<TextSegment> retrieve(String query) {
        return retrieve(query, defaultTopK);
    }

    /**
     * 检索相关文档片段（指定 Top-K）
     */
    public List<TextSegment> retrieve(String query, int topK) {
        log.info("检索: '{}', topK={}", query, topK);
        return vectorStoreService.search(query, topK);
    }

    /**
     * 检索并拼接为上下文文本
     */
    public String retrieveAsContext(String query) {
        return retrieveAsContext(query, defaultTopK);
    }

    /**
     * 检索并拼接为上下文文本（指定 Top-K）
     */
    public String retrieveAsContext(String query, int topK) {
        List<TextSegment> segments = retrieve(query, topK);

        if (segments.isEmpty()) {
            return "未找到相关文档";
        }

        StringBuilder context = new StringBuilder("相关文档内容：\n\n");
        for (int i = 0; i < segments.size(); i++) {
            context.append("【片段 ").append(i + 1).append("】\n");
            context.append(segments.get(i).text()).append("\n\n");
        }
        return context.toString();
    }
}