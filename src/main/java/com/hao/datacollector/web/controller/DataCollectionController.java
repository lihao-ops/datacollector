package com.hao.datacollector.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据采集控制器
 * 提供股票、期货、基金等金融数据采集接口
 * 
 * @author hao
 */
@Slf4j
@RestController
@RequestMapping("/data_collection")
@Tag(name = "数据采集", description = "股票、期货、基金等金融数据采集接口")
public class DataCollectionController {

    @Operation(
        summary = "获取股票实时数据",
        description = "根据股票代码获取实时股票数据，包括价格、成交量等信息"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "成功获取股票数据",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "股票代码不存在"
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "服务器内部错误"
        )
    })
    @GetMapping("/stock/{stockCode}")
    public ResponseEntity<Map<String, Object>> getStockData(
            @Parameter(description = "股票代码，如：000001.SZ", example = "000001.SZ")
            @PathVariable String stockCode) {
        
        log.info("获取股票数据，股票代码: {}", stockCode);
        
        Map<String, Object> result = new HashMap<>();
        result.put("stockCode", stockCode);
        result.put("price", 15.68);
        result.put("volume", 1000000);
        result.put("timestamp", LocalDateTime.now());
        result.put("status", "success");
        
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "启动数据采集任务",
        description = "启动指定类型的数据采集任务"
    )
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startCollection(
            @Parameter(description = "数据类型", example = "stock")
            @RequestParam String dataType,
            @Parameter(description = "采集频率（秒）", example = "60")
            @RequestParam(defaultValue = "60") Integer frequency) {
        
        log.info("启动数据采集任务，数据类型: {}, 频率: {}秒", dataType, frequency);
        
        Map<String, Object> result = new HashMap<>();
        result.put("taskId", "TASK_" + System.currentTimeMillis());
        result.put("dataType", dataType);
        result.put("frequency", frequency);
        result.put("status", "started");
        result.put("startTime", LocalDateTime.now());
        
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "获取采集任务状态",
        description = "查询数据采集任务的运行状态"
    )
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getCollectionStatus() {
        
        log.info("查询数据采集任务状态");
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalTasks", 5);
        result.put("runningTasks", 3);
        result.put("completedTasks", 2);
        result.put("lastUpdateTime", LocalDateTime.now());
        
        return ResponseEntity.ok(result);
    }
}