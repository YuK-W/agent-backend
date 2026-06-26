package com.yuke.agentbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentTask {

    private String taskId;
    private String userTask;
    private String status;  // PENDING, RUNNING, COMPLETED, FAILED
    private String result;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private Long executionTimeMs;

    public enum Status {
        PENDING,
        RUNNING,
        COMPLETED,
        FAILED
    }
}