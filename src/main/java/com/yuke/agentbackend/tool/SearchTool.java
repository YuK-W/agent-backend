package com.yuke.agentbackend.tool;

import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SearchTool {

    /**
     * 模拟搜索（可替换为真实搜索引擎 API）
     */
    private static final List<String> DEMO_DATA = new ArrayList<>();

    static {
        DEMO_DATA.add("Spring Boot 是 Spring 框架的扩展，用于简化微服务开发");
        DEMO_DATA.add("Java 17 引入了密封类、模式匹配等新特性");
        DEMO_DATA.add("Docker 容器化技术是现代 DevOps 的核心工具");
        DEMO_DATA.add("Kubernetes 是容器编排的事实标准");
        DEMO_DATA.add("RAG 技术结合了检索和生成，提高大模型准确性");
    }

    /**
     * 执行搜索
     *
     * @param query 搜索关键词
     * @return 搜索结果
     */
    @Tool("执行网络搜索，获取相关信息")
    public String search(String query) {
        log.info("🔍 搜索工具被调用: query={}", query);

        if (query == null || query.trim().isEmpty()) {
            return "错误：搜索关键词不能为空";
        }

        // 简单关键词匹配（演示用）
        List<String> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (String item : DEMO_DATA) {
            if (item.toLowerCase().contains(lowerQuery)) {
                results.add(item);
            }
        }

        if (results.isEmpty()) {
            // 没有匹配时返回一些默认结果
            return String.format("关于 '%s' 的搜索结果：暂无直接匹配。建议尝试其他关键词。", query);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("搜索结果：\n");
        for (int i = 0; i < results.size(); i++) {
            sb.append(i + 1).append(". ").append(results.get(i)).append("\n");
        }
        return sb.toString();
    }
}