package com.yuke.agentbackend.tool;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolResult {
    private String toolName;
    private String result;
    private boolean success;
    private String errorMessage;
}