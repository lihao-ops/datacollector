package com.hao.datacollector.web.controller;

import com.hao.datacollector.service.BaseDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-06-14 15:55:50
 * @description: 基础数据处理controller
 */
@Tag(name = "基础数据", description = "基础数据处理接口")
@Slf4j
@RestController
@RequestMapping("/base_date")
public class BaseDataController {
    @Autowired
    private BaseDataService baseDataService;

    @Operation(
            summary = "交易日历",
            description = "转档交易日历"
    )
    @PostMapping("/set_trade_date")
    public Boolean setTradeDateList(
            @Parameter(description = "起始日", example = "日历起始日:例如20250614")
            @RequestParam String startTime,
            @Parameter(description = "终止日", example = "60")
            @RequestParam(defaultValue = "20250614") String endTime) {
        log.info("接收到交易日历转档请求,startTime={},endTime={}", startTime, endTime);
        Boolean result = baseDataService.setTradeDateList(startTime, endTime);
        log.info("涨停转档请求处理完成={}", result);
        return result;
    }
}
