package com.yuke.agentbackend.agent;

import com.yuke.agentbackend.llm.LlmProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReviewerAgent extends BaseAgent {

    private static final String REVIEWER_PROMPT = """
            你是一个资深代码审查专家。
            你的职责是：
            1. 审查代码质量
            2. 检查潜在 bug 和性能问题
            3. 提出改进建议
            4. 给出整体评分（1-10）

            输出格式（必须严格遵守）：
            ## 代码审查报告

            ### 优点
            [列出代码的优点]

            ### 问题
            [列出发现的问题]

            ### 改进建议
            [给出具体改进建议]

            ### 总体评分
            [评分/10]
            """;

    public ReviewerAgent(LlmProvider llmProvider) {
        super("Reviewer", "代码审查专家", REVIEWER_PROMPT, llmProvider);
    }

    @Override
    public String execute(String input) {
        log.info("🔍 ReviewerAgent 开始审查代码");
        String result = super.execute(input);
        log.info("🔍 ReviewerAgent 审查完成");
        return result;
    }
}
