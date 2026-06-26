package com.yuke.agentbackend.controller;

import com.yuke.agentbackend.agent.Orchestrator;
import com.yuke.agentbackend.model.AgentTask;
import com.yuke.agentbackend.model.ChatRequest;
import com.yuke.agentbackend.model.ChatResponse;
import com.yuke.agentbackend.model.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {

    private final Orchestrator orchestrator;

    // 内存存储任务状态（生产环境应使用 Redis 或数据库）
    private final Map<String, AgentTask> taskStore = new ConcurrentHashMap<>();

    /**
     * 提交多 Agent 任务
     * POST /api/agent/task
     * {"message": "写一个二分查找算法"}
     */
    @PostMapping("/task")
    public CommonResponse<AgentTask> submitTask(@Valid @RequestBody ChatRequest request) {
        String taskId = UUID.randomUUID().toString();
        log.info("📥 收到多 Agent 任务: taskId={}, message={}", taskId, request.getMessage());

        AgentTask task = AgentTask.builder()
                .taskId(taskId)
                .userTask(request.getMessage())
                .status(AgentTask.Status.PENDING.name())
                .createdAt(LocalDateTime.now())
                .build();

        taskStore.put(taskId, task);

        // 异步执行（这里简化，同步执行）
        // 生产环境应使用 @Async 或线程池
        try {
            task.setStatus(AgentTask.Status.RUNNING.name());
            taskStore.put(taskId, task);

            long startTime = System.currentTimeMillis();
            String result = orchestrator.executePipeline(request.getMessage());
            long endTime = System.currentTimeMillis();

            task.setStatus(AgentTask.Status.COMPLETED.name());
            task.setResult(result);
            task.setCompletedAt(LocalDateTime.now());
            task.setExecutionTimeMs(endTime - startTime);
            taskStore.put(taskId, task);

            log.info("✅ Agent 任务完成: taskId={}", taskId);

        } catch (Exception e) {
            log.error("❌ Agent 任务失败: taskId={}", taskId, e);
            task.setStatus(AgentTask.Status.FAILED.name());
            task.setErrorMessage(e.getMessage());
            taskStore.put(taskId, task);
        }

        return CommonResponse.success(task);
    }

    /**
     * 查询任务状态
     * GET /api/agent/status/{taskId}
     */
    @GetMapping("/status/{taskId}")
    public CommonResponse<AgentTask> getTaskStatus(@PathVariable String taskId) {
        log.info("📊 查询任务状态: taskId={}", taskId);

        AgentTask task = taskStore.get(taskId);
        if (task == null) {
            return CommonResponse.error(404, "任务不存在: " + taskId);
        }

        return CommonResponse.success(task);
    }

    /**
     * 获取所有任务列表
     * GET /api/agent/tasks
     */
    @GetMapping("/tasks")
    public CommonResponse<Map<String, AgentTask>> getAllTasks() {
        return CommonResponse.success(taskStore);
    }
}