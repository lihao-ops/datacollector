package com.hao.datacollector.dto.limitup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for daily_stock_summary table
 */
@Data
@Schema(description = "每日股票统计概要DTO")
public class DailyStockSummaryDTO {

    @Schema(description = "交易日期", example = "2023-10-01")
    private LocalDate tradeDate;

    @Schema(description = "功能ID", example = "1")
    private String functionId;

    @Schema(description = "总数量", example = "3000")
    private Integer totalNum;

    @Schema(description = "涨停数量", example = "100")
    private Integer limitUpNum;

    @Schema(description = "跌停数量", example = "10")
    private Integer limitDownNum;

    @Schema(description = "上涨数量", example = "1500")
    private Integer upNum;

    @Schema(description = "下跌数量", example = "1200")
    private Integer downNum;

    @Schema(description = "平盘数量", example = "300")
    private Integer flatNum;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}