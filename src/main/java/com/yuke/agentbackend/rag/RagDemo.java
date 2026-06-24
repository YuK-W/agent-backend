package com.yuke.agentbackend.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class RagDemo implements CommandLineRunner {

    private final DocumentLoader documentLoader;
    private final DocumentSplitter documentSplitter;
    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;
    private final Retriever retriever;

    @Override
    public void run(String... args) {
        log.info("========================================");
        log.info("🧪 开始 RAG 基础功能测试");
        log.info("========================================");

        // 1. 准备测试文档
        String testContent = """
                Java 是一种面向对象的编程语言，由 Sun Microsystems 公司于 1995 年推出。
                Java 的特点是"一次编写，到处运行"，这得益于 Java 虚拟机 (JVM) 的跨平台特性。
                
                Spring Boot 是 Spring 框架的一个子项目，用于快速构建独立的、生产级的 Spring 应用程序。
                Spring Boot 提供了自动配置、嵌入式服务器等特性，大大简化了开发流程。
                
                多智能体系统 (Multi-Agent System) 是由多个智能体组成的系统，每个智能体能够自主决策和行动。
                在多智能体系统中，智能体之间通过通信和协作来完成复杂的任务。
                
                RAG (Retrieval-Augmented Generation) 是一种结合了检索和生成的 AI 技术。
                RAG 通过先从知识库中检索相关信息，再让大模型基于这些信息生成回答，提高了回答的准确性。
                """;

        // 2. 加载文档
        Document document = documentLoader.loadDocumentFromText(testContent, "test-doc.txt");
        log.info("✅ 文档加载完成");

        // 3. 分块
        List<TextSegment> segments = documentSplitter.split(document);
        log.info("✅ 文档分割完成，共 {} 个片段", segments.size());

        // 4. 向量化 - 直接传入 segments
        List<Embedding> embeddings = embeddingService.embedAll(segments);
        log.info("✅ 向量化完成，维度: {}", embeddingService.dimension());

        // 5. 存储到向量库
        vectorStoreService.addAll(segments, embeddings);
        log.info("✅ 存储完成，共 {} 个向量", segments.size());

        // 6. 检索测试
        log.info("========================================");
        log.info("🔍 开始检索测试");
        log.info("========================================");

        String query1 = "什么是 Java？";
        log.info("查询: {}", query1);
        String context1 = retriever.retrieveAsContext(query1);
        log.info("检索结果:\n{}", context1);

        String query2 = "什么是 RAG？";
        log.info("查询: {}", query2);
        String context2 = retriever.retrieveAsContext(query2);
        log.info("检索结果:\n{}", context2);

        String query3 = "Spring Boot 有什么特点？";
        log.info("查询: {}", query3);
        String context3 = retriever.retrieveAsContext(query3);
        log.info("检索结果:\n{}", context3);

        log.info("========================================");
        log.info("✅ RAG 基础功能测试完成！");
        log.info("========================================");
    }
}
