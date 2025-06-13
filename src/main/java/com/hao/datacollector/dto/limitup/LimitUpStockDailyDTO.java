package com.hao.datacollector.dto.limitup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for limit_up_stock_daily table
 */
@Data
@Schema(description = "每日涨停股票信息DTO")
public class LimitUpStockDailyDTO {

    @Schema(description = "交易日期", example = "2023-10-01")
    private LocalDate tradeDate;

    @Schema(description = "Wind代码", example = "600519.SH")
    private String windCode;

    @Schema(description = "股票代码", example = "600519")
    private String code;

    @Schema(description = "股票名称", example = "贵州茅台")
    private String name;

    @Schema(description = "收盘价", example = "1800.00")
    private BigDecimal closePrice;

    @Schema(description = "前收盘价", example = "1780.00")
    private BigDecimal preClosePrice;

    @Schema(description = "涨跌幅", example = "1.12")
    private BigDecimal changePct;

    @Schema(description = "振幅", example = "2.5")
    private BigDecimal amplitude;

    @Schema(description = "换手率", example = "0.5")
    private BigDecimal turnoverRate;

    @Schema(description = "成交量(股)", example = "1000000")
    private BigDecimal volume;

    @Schema(description = "成交额(元)", example = "1800000000")
    private BigDecimal turnover;

    @Schema(description = "流通市值(元)", example = "2000000000000")
    private BigDecimal floatMarketCap;

    @Schema(description = "总市值(元)", example = "2200000000000")
    private BigDecimal totalMarketCap;

    @Schema(description = "连板天数", example = "3")
    private Integer limitUpDays;

    @Schema(description = "首次涨停时间", example = "09:30:00")
    private String firstLimitUpTime;

    @Schema(description = "最后涨停时间", example = "14:55:00")
    private String lastLimitUpTime;

    @Schema(description = "开板次数", example = "1")
    private Integer openCount;

    @Schema(description = "涨停类型", example = "1")
    private BigDecimal limitUpType;

    @Schema(description = "所属行业名称", example = "食品饮料")
    private String industryName;

    @Schema(description = "是否新股", example = "N")
    private String isNewStock;

    @Schema(description = "是否ST股", example = "N")
    private String isStStock;

    @Schema(description = "是否停牌", example = "N")
    private String isSuspend;

    @Schema(description = "涨停原因")
    private String reasonForLimitUp;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}