package com.yuke.agentbackend.agent;

public interface Agent {

    String getName();

    String getRole();

    String execute(String input);

    default String execute(String input, String context) {
        return execute(input);
    }
}