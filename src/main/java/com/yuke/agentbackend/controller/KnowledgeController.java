package com.yuke.agentbackend.controller;

import com.yuke.agentbackend.model.CommonResponse;
import com.yuke.agentbackend.rag.DocumentLoader;
import com.yuke.agentbackend.rag.DocumentSplitter;
import com.yuke.agentbackend.rag.EmbeddingService;
import com.yuke.agentbackend.rag.Retriever;
import com.yuke.agentbackend.rag.VectorStoreService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class KnowledgeController {

    private final DocumentLoader documentLoader;
    private final DocumentSplitter documentSplitter;
    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;
    private final Retriever retriever;

    /**
     * 上传文档入库
     * POST /api/knowledge/upload
     */
    @PostMapping("/upload")
    public CommonResponse<Map<String, Object>> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            log.info("上传文档: {}", file.getOriginalFilename());

            // 读取文件内容
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);

            // 加载文档
            Document document = documentLoader.loadDocumentFromText(content, file.getOriginalFilename());

            // 分块
            List<TextSegment> segments = documentSplitter.split(document);
            log.info("文档分割为 {} 个片段", segments.size());

            // 向量化
            List<Embedding> embeddings = embeddingService.embedAll(segments);

            // 存储
            vectorStoreService.addAll(segments, embeddings);

            Map<String, Object> result = new HashMap<>();
            result.put("fileName", file.getOriginalFilename());
            result.put("chunks", segments.size());
            result.put("status", "success");

            return CommonResponse.success(result);

        } catch (IOException e) {
            log.error("文档上传失败", e);
            return CommonResponse.error(500, "文档上传失败: " + e.getMessage());
        }
    }

    /**
     * 语义检索（调试用）
     * GET /api/knowledge/search?q=xxx
     */
    @GetMapping("/search")
    public CommonResponse<Map<String, Object>> search(@RequestParam("q") String query) {
        log.info("语义检索: {}", query);

        List<TextSegment> results = retriever.retrieve(query);

        Map<String, Object> result = new HashMap<>();
        result.put("query", query);
        result.put("results", results.stream()
                .map(TextSegment::text)
                .toList());
        result.put("count", results.size());

        return CommonResponse.success(result);
    }

    /**
     * 清空知识库
     * DELETE /api/knowledge/clear
     */
    @DeleteMapping("/clear")
    public CommonResponse<String> clear() {
        log.info("清空知识库");
        vectorStoreService.clear();
        return CommonResponse.success("知识库已清空");
    }

    /**
     * 获取知识库统计
     * GET /api/knowledge/stats
     */
    @GetMapping("/stats")
    public CommonResponse<Map<String, Object>> stats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("type", "InMemoryEmbeddingStore");
        stats.put("status", "ready");
        return CommonResponse.success(stats);
    }
}
