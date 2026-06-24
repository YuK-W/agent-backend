package com.yuke.agentbackend.rag;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VectorStoreService {

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingService embeddingService;

    /**
     * 存储单个文本片段
     */
    public void add(TextSegment segment, Embedding embedding) {
        log.debug("存储文本片段，长度: {}", segment.text().length());
        embeddingStore.add(embedding, segment);
    }

    /**
     * 批量存储文本片段
     */
    public void addAll(List<TextSegment> segments, List<Embedding> embeddings) {
        log.info("批量存储 {} 个文本片段", segments.size());
        embeddingStore.addAll(embeddings, segments);
    }

    /**
     * 语义检索
     */
    public List<TextSegment> search(String query, int topK) {
        log.info("语义检索: '{}', topK={}", query, topK);

        Embedding queryEmbedding = embeddingService.embed(query);

        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(topK)
                .minScore(0.5) // 最低相似度阈值
                .build();

        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(request);

        log.info("检索到 {} 个相关片段", result.matches().size());

        return result.matches().stream()
                .map(match -> match.embedded())
                .toList();
    }

    /**
     * 获取向量存储中的文档数量
     */
    public int count() {
        // InMemoryEmbeddingStore 没有直接提供 count 方法，暂时返回 -1
        return -1;
    }

    /**
     * 清空向量存储
     */
    public void clear() {
        log.info("清空向量存储");
        embeddingStore.removeAll();
    }
}