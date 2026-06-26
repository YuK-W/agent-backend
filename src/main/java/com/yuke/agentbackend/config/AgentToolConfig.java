package com.yuke.agentbackend.config;

import com.yuke.agentbackend.tool.SearchTool;
import com.yuke.agentbackend.tool.WeatherTool;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Agent 工具配置
 * 将 @Tool 方法注册到 LangChain4j 的 AiServices
 */
@Slf4j
@Configuration
public class AgentToolConfig {

    /**
     * 获取所有注册的工具规范（用于手动注册）
     */
    public static List<ToolSpecification> getToolSpecifications(
            WeatherTool weatherTool,
            SearchTool searchTool) {

        return Arrays.asList(
                getToolSpecification(weatherTool, "getWeather"),
                getToolSpecification(weatherTool, "getSupportedCities"),
                getToolSpecification(searchTool, "search")
        );
    }

    /**
     * 从 @Tool 注解中提取 ToolSpecification
     */
    private static ToolSpecification getToolSpecification(Object instance, String methodName) {
        try {
            Method method = instance.getClass().getMethod(methodName, getParameterTypes(methodName));
            Tool toolAnnotation = method.getAnnotation(Tool.class);

            if (toolAnnotation == null) {
                throw new IllegalArgumentException("方法 " + methodName + " 没有 @Tool 注解");
            }

            String[] values = toolAnnotation.value();
            String description = (values != null && values.length > 0) ? values[0] : methodName;
            return ToolSpecification.builder()
                    .name(methodName)
                    .description(description)
                    .build();

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("找不到方法: " + methodName, e);
        }
    }

    private static Class<?>[] getParameterTypes(String methodName) {
        return switch (methodName) {
            case "getWeather", "search" -> new Class[]{String.class};
            case "getSupportedCities" -> new Class[]{};
            default -> new Class[]{};
        };
    }

    /**
     * 创建带工具的 Chat Agent（演示 AiServices 用法）
     */
    @Bean
    @Lazy
    public Object agentWithTools(
            ChatLanguageModel chatLanguageModel,
            WeatherTool weatherTool,
            SearchTool searchTool) {

        log.info("🔧 初始化 AiServices（带工具绑定）");

        // 使用 AiServices 创建带工具的智能体
        // 这里演示用，后续可以扩展为特定角色的 Agent
        return AiServices.builder(AssistantWithTools.class)
                .chatLanguageModel(chatLanguageModel)
                .tools(weatherTool, searchTool)
                .build();
    }

    /**
     * 带工具的 AI 助手接口
     * LangChain4j 会自动将 @Tool 方法与 LLM 的 Function Calling 对接
     */
    public interface AssistantWithTools {

        /**
         * 带系统提示词的对话
         */
        @SystemMessage("""
                你是一个智能助手，可以调用工具来帮助用户。
                你可以查询天气、搜索信息等。
                当用户问到天气时，使用 getWeather 工具。
                当用户需要查询信息时，使用 search 工具。
                如果用户问的是其他问题，直接回答。
                """)
        String chat(String userMessage);
    }
}