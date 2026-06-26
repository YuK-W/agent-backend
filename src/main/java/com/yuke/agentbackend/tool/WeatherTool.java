package com.yuke.agentbackend.tool;

import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 天气查询工具
 * 使用 @Tool 注解注册到 LangChain4j
 */
@Slf4j
@Component
public class WeatherTool {

    /**
     * 模拟天气数据（实际生产可调用真实天气 API）
     */
    private static final Map<String, String> WEATHER_CACHE = new HashMap<>();

    static {
        WEATHER_CACHE.put("北京", "晴转多云，25°C，东北风3级");
        WEATHER_CACHE.put("上海", "阴有小雨，22°C，东南风2级");
        WEATHER_CACHE.put("广州", "雷阵雨，28°C，南风4级");
        WEATHER_CACHE.put("深圳", "多云，27°C，西南风3级");
        WEATHER_CACHE.put("杭州", "晴，26°C，东风2级");
        WEATHER_CACHE.put("成都", "多云，24°C，北风2级");
        WEATHER_CACHE.put("武汉", "小雨，23°C，东北风3级");
        WEATHER_CACHE.put("西安", "晴，22°C，西北风2级");
        WEATHER_CACHE.put("南京", "阴，25°C，东风2级");
        WEATHER_CACHE.put("重庆", "雾转多云，23°C，南风1级");
    }

    /**
     * 查询指定城市的天气
     *
     * @param city 城市名称（如：北京、上海）
     * @return 天气信息
     */
    @Tool("查询指定城市的天气信息，参数为城市名称")
    public String getWeather(String city) {
        log.info("🌤️ 天气查询工具被调用: city={}", city);

        if (city == null || city.trim().isEmpty()) {
            return "错误：城市名称不能为空";
        }

        String weather = WEATHER_CACHE.get(city.trim());
        if (weather == null) {
            // 没有缓存数据时，返回默认信息
            return String.format("暂无 %s 的实时天气数据，建议查询：北京、上海、广州、深圳、杭州、成都、武汉、西安、南京、重庆",
                    city);
        }

        return String.format("%s 今日天气：%s", city, weather);
    }

    /**
     * 获取支持的城市列表
     */
    @Tool("获取天气查询支持的城市列表")
    public String getSupportedCities() {
        log.info("🌤️ 获取支持的城市列表");
        return "支持的城市: " + String.join("、", WEATHER_CACHE.keySet());
    }
}