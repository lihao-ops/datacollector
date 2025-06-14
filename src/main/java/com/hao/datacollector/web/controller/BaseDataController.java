package com.hao.datacollector.web.controller;

import com.hao.datacollector.common.constant.DateTimeFormatConstant;
import com.hao.datacollector.common.utils.DateUtil;
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

import java.time.LocalDate;
import java.util.List;

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
            summary = "转档交易日历",
            description = "根据时间区间转档交易日历数据(注：会清空原有表数据)"
    )
    @PostMapping("/set_trade_date")
    public Boolean setTradeDateList(
            @Parameter(description = "起始日", example = "日历起始日:例如20250614")
            @RequestParam String startTime,
            @Parameter(description = "终止日", example = "60")
            @RequestParam(defaultValue = "20250614") String endTime) {
        log.info("setTradeDateList,startTime={},endTime={}", startTime, endTime);
        return baseDataService.setTradeDateList(startTime, endTime);
    }

    @Operation(
            summary = "获取交易日历",
            description = "根据时间区间获取交易日历"
    )
    @PostMapping("/get_trade_date")
    public List<String> getTradeDateListByTime(
            @Parameter(description = "起始日", example = "日历起始日:例如20250614")
            @RequestParam String startTime,
            @Parameter(description = "终止日", example = "60")
            @RequestParam(defaultValue = "20250614") String endTime) {
        log.info("getTradeDateListByTime,startTime={},endTime={}", startTime, endTime);
        List<LocalDate> dateListByTime = baseDataService.getTradeDateListByTime(startTime, endTime);
        //默认转换为8位数字日期格式（如：20190214）
        return DateUtil.formatLocalDateList(dateListByTime,  DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT);
    }
}
