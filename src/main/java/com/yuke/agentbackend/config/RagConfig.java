package com.yuke.agentbackend.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RagConfig {

    @Value("${rag.chunk-size:500}")
    private int chunkSize;

    @Value("${rag.chunk-overlap:50}")
    private int chunkOverlap;

    @Value("${rag.top-k:4}")
    private int topK;

    @Value("${rag.vector-store:memory}")
    private String vectorStoreType;

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        log.info("初始化向量存储: {}", vectorStoreType);
        if ("memory".equalsIgnoreCase(vectorStoreType)) {
            // 使用内存向量存储（零依赖，适合开发测试）
            return new InMemoryEmbeddingStore<>();
        }
        // 后续可扩展 Chroma、PGVector 等
        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    public int chunkSize() {
        return chunkSize;
    }

    @Bean
    public int chunkOverlap() {
        return chunkOverlap;
    }

    @Bean
    public int topK() {
        return topK;
    }
}