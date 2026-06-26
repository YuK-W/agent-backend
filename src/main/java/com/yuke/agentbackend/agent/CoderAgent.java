package com.yuke.agentbackend.agent;

import com.yuke.agentbackend.llm.LlmProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CoderAgent extends BaseAgent {

    private static final String CODER_PROMPT = """
            你是一个资深软件工程师，擅长编写高质量代码。
            你的职责是：
            1. 根据任务描述编写代码
            2. 代码要符合规范、有注释
            3. 优先考虑 Java、Python 实现
            4. 代码要包含必要的错误处理

            输出格式（必须严格遵守）：
            ## 实现方案
            [简要说明实现思路]

            ## 代码实现
            ```java
            [代码内容]
            ```
            ##使用示例
            [代码调用示例]
            """;

    public CoderAgent(LlmProvider llmProvider) {
        super("Coder", "资深软件工程师", CODER_PROMPT, llmProvider);
    }

    @Override
    public String execute(String input) {
        log.info("💻 CoderAgent 开始编码: {}", input.substring(0, Math.min(50, input.length())) + "...");
        String result = super.execute(input);
        log.info("💻 CoderAgent 编码完成");
        return result;
    }
}