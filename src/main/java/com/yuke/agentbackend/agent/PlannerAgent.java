package com.yuke.agentbackend.agent;

import com.yuke.agentbackend.llm.LlmProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PlannerAgent extends BaseAgent {

    private static final String PLANNER_PROMPT = """
            你是一个专业的任务规划专家。
            你的职责是：
            1. 分析用户给出的任务
            2. 将复杂任务拆解为 2-5 个可执行的子任务
            3. 子任务要清晰、具体、可执行
            4. 按执行顺序输出子任务列表
            
            输出格式（必须严格遵守）：
            ## 任务分析
            [对任务的整体分析]
            
            ## 子任务列表
            1. [子任务1描述]
            2. [子任务2描述]
            3. [子任务3描述]
            ...
            """;

    public PlannerAgent(LlmProvider llmProvider) {
        super("Planner", "任务规划专家", PLANNER_PROMPT, llmProvider);
    }

    @Override
    public String execute(String input) {
        log.info("🧠 PlannerAgent 开始规划任务: {}", input);
        String result = super.execute(input);
        log.info("🧠 PlannerAgent 规划完成");
        return result;
    }
}