package com.yuke.agentbackend.rag;

import com.yuke.agentbackend.llm.LlmProvider;
import dev.langchain4j.data.segment.TextSegment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RagService {

    private final Retriever retriever;
    private final LlmProvider llmProvider;

    @Value("${rag.top-k:4}")
    private int topK;

    /**
     * RAG 完整流程：检索 + 生成
     */
    public String ragQuery(String question) {
        log.info("RAG 查询: {}", question);

        // 1. 检索相关文档片段
        List<TextSegment> segments = retriever.retrieve(question, topK);
        log.info("检索到 {} 个相关片段", segments.size());

        // 2. 构建上下文
        String context = buildContext(segments);

        // 3. 构建 Prompt
        String prompt = buildPrompt(question, context);

        // 4. 调用 LLM 生成回答
        String answer = llmProvider.chat(prompt);
        log.info("RAG 回答生成完成");

        return answer;
    }

    /**
     * RAG 查询（返回带来源的完整结果）
     */
    public RagResult ragQueryWithSources(String question) {
        log.info("RAG 查询（带来源）: {}", question);

        // 1. 检索
        List<TextSegment> segments = retriever.retrieve(question, topK);

        // 2. 构建上下文
        String context = buildContext(segments);

        // 3. 构建 Prompt
        String prompt = buildPrompt(question, context);

        // 4. 调用 LLM
        String answer = llmProvider.chat(prompt);

        // 5. 提取来源
        List<String> sources = segments.stream()
                .map(TextSegment::text)
                .collect(Collectors.toList());

        return RagResult.builder()
                .answer(answer)
                .sources(sources)
                .build();
    }

    private String buildContext(List<TextSegment> segments) {
        if (segments.isEmpty()) {
            return "未找到相关文档内容。";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < segments.size(); i++) {
            sb.append("【文档片段 ").append(i + 1).append("】\n");
            sb.append(segments.get(i).text()).append("\n\n");
        }
        return sb.toString();
    }

    private String buildPrompt(String question, String context) {
        return """
                你是一个知识助手，请根据以下提供的文档内容回答用户的问题。
                
                ## 文档内容
                %s
                
                ## 用户问题
                %s
                
                ## 回答要求
                1. 只根据提供的文档内容回答，不要使用外部知识
                2. 如果文档中没有相关信息，请如实告知
                3. 回答要准确、简洁、有条理
                """.formatted(context, question);
    }

    /**
     * RAG 结果内部类
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class RagResult {
        private String answer;
        private List<String> sources;
    }
}