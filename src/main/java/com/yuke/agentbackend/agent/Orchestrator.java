package com.yuke.agentbackend.agent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Orchestrator {

    private final PlannerAgent plannerAgent;
    private final CoderAgent coderAgent;
    private final ReviewerAgent reviewerAgent;

    /**
     * 执行多 Agent 流水线
     * 用户任务 → Planner拆解 → Coder编码 → Reviewer审查 → 汇总返回
     */
    public String executePipeline(String userTask) {
        log.info("========================================");
        log.info("🚀 开始多 Agent 流水线执行");
        log.info("📝 用户任务: {}", userTask);
        log.info("========================================");

        long startTime = System.currentTimeMillis();

        try {
            // 阶段 1: Planner - 任务规划
            log.info("【阶段 1/3】Planner 任务规划");
            String plan = plannerAgent.execute(userTask);

            // 阶段 2: Coder - 代码生成
            log.info("【阶段 2/3】Coder 代码生成");
            String code = coderAgent.execute(userTask, plan);

            // 阶段 3: Reviewer - 代码审查
            log.info("【阶段 3/3】Reviewer 代码审查");
            String review = reviewerAgent.execute(code);

            long endTime = System.currentTimeMillis();
            log.info("========================================");
            log.info("✅ 多 Agent 流水线执行完成，耗时: {} ms", endTime - startTime);
            log.info("========================================");

            // 汇总结果
            return buildSummary(userTask, plan, code, review);

        } catch (Exception e) {
            log.error("多 Agent 流水线执行失败", e);
            return "❌ 任务执行失败: " + e.getMessage();
        }
    }

    private String buildSummary(String userTask, String plan, String code, String review) {
        StringBuilder summary = new StringBuilder();
        summary.append("========================================\n");
        summary.append("📋 多 Agent 协作报告\n");
        summary.append("========================================\n\n");

        summary.append("📌 原始任务: ").append(userTask).append("\n\n");

        summary.append("【Planner 任务规划】\n");
        summary.append(plan).append("\n\n");

        summary.append("【Coder 代码实现】\n");
        summary.append(code).append("\n\n");

        summary.append("【Reviewer 代码审查】\n");
        summary.append(review).append("\n\n");

        summary.append("========================================\n");
        summary.append("✅ 多 Agent 协作完成！\n");
        summary.append("========================================\n");

        return summary.toString();
    }
}