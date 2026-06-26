package com.yuke.agentbackend.controller;

import com.yuke.agentbackend.model.CommonResponse;
import com.yuke.agentbackend.tool.SearchTool;
import com.yuke.agentbackend.tool.WeatherTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/tools")
@RequiredArgsConstructor
public class ToolController {

    private final WeatherTool weatherTool;
    private final SearchTool searchTool;

    /**
     * 测试天气查询
     * GET /api/tools/weather?city=北京
     */
    @GetMapping("/weather")
    public CommonResponse<String> getWeather(@RequestParam String city) {
        log.info("🌤️ 查询天气: {}", city);
        String result = weatherTool.getWeather(city);
        return CommonResponse.success(result);
    }

    /**
     * 获取支持的城市列表
     * GET /api/tools/weather/cities
     */
    @GetMapping("/weather/cities")
    public CommonResponse<String> getSupportedCities() {
        log.info("🌤️ 获取支持的城市列表");
        String result = weatherTool.getSupportedCities();
        return CommonResponse.success(result);
    }

    /**
     * 测试搜索
     * GET /api/tools/search?q=Spring
     */
    @GetMapping("/search")
    public CommonResponse<String> search(@RequestParam("q") String query) {
        log.info("🔍 搜索: {}", query);
        String result = searchTool.search(query);
        return CommonResponse.success(result);
    }
}