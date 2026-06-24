package com.yuke.agentbackend.rag;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EmbeddingService {

    private EmbeddingModel embeddingModel;

    @Value("${llm.embedding.base-url}")
    private String baseUrl;

    @Value("${llm.embedding.api-key}")
    private String apiKey;

    @Value("${llm.embedding.model-name}")
    private String embeddingModelName;

    @PostConstruct
    public void init() {
        log.info("初始化 Embedding Model: {}", embeddingModelName);
        this.embeddingModel = OpenAiEmbeddingModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(embeddingModelName)
                .build();
    }

    /**
     * 对单个文本进行向量化
     */
    public Embedding embed(String text) {
        log.debug("向量化文本，长度: {}", text.length());
        return embeddingModel.embed(text).content();
    }

    /**
     * 批量向量化 - 接收 TextSegment 列表
     */
    public List<Embedding> embedAll(List<TextSegment> segments) {
        log.info("批量向量化，数量: {}", segments.size());
        // embedAll 返回的是 Response<List<EmbeddingResult>>
        var response = embeddingModel.embedAll(segments);
        // 先 .content() 拿到 List<EmbeddingResult>，再 .stream()
        // 每个 result 是 EmbeddingResult，调用 .content() 得到 Embedding
        return response.content();
    }

    /**
     * 获取向量维度
     */
    public int dimension() {
        return embeddingModel.dimension();
    }
}