package com.yuke.agentbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgentBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentBackendApplication.class, args);
        System.out.println("========================================");
        System.out.println("  🤖 多智能体AI助手后端系统启动成功！");
        System.out.println("  📡 访问: http://localhost:8080");
        System.out.println("========================================");
    }
}