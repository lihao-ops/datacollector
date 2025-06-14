package com.hao.datacollector.dto.table.limitup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "每日涨停股票明细插入对象")
public class LimitUpStockDailyInsertDTO {

    @Schema(description = "交易日", example = "2024-06-14")
    private String tradeDate;

    @Schema(description = "股票代码(SH/SZ后缀)", example = "600519.SH")
    private String windCode;

    @Schema(description = "股票名称", example = "贵州茅台")
    private String name;

    @Schema(description = "首次涨停时间(HHMMSS)", example = "093015")
    private String firstTime;

    @Schema(description = "连板描述(如6天5板)", example = "6天5板")
    private String statusDesc;

    @Schema(description = "涨停封单额(元)")
    private Double orderTotal;

    @Schema(description = "主力流入(元)")
    private Double volumeNetin;

    @Schema(description = "流通市值(元)")
    private Double listedStock;

    @Schema(description = "当前价格")
    private Double price;

    @Schema(description = "换手率(%)")
    private Double turnoverRate;

    @Schema(description = "开板时间(HHMMSS)")
    private String openTime;

    @Schema(description = "开板次数")
    private Integer openTimes;

    @Schema(description = "涨幅(%)")
    private Double changeRange;

    @Schema(description = "5分钟涨速(%)")
    private Double changeRange5;

    @Schema(description = "最高5分钟涨速(%)")
    private Double changeRange5Max;

    @Schema(description = "1分钟涨速(%)")
    private Double changeRange1;

    @Schema(description = "最高1分钟涨速(%)")
    private Double changeRange1Max;

    @Schema(description = "竞价涨停价委托买(元)")
    private Double callAuctionOrderTotal;

    @Schema(description = "连板X系数")
    private Integer limitUpX;

    @Schema(description = "连板N系数")
    private Integer limitUpN;

    @Schema(description = "连板M系数")
    private Integer limitUpM;

    @Schema(description = "标签热度和")
    private Double topicHotSum;

    @Schema(description = "低位涨幅(%)")
    private Double lowRange;

    @Schema(description = "主力强度")
    private Double mainForces;

    @Schema(description = "主力成本(元)")
    private Double cost;

    @Schema(description = "主力浮盈(%)")
    private Double profit;

    @Schema(description = "主力流入(元)")
    private Double mainForcesIn;

    @Schema(description = "主力分歧度")
    private Double divergency;

    @Schema(description = "主力时间(s)")
    private Integer mainForcesTime;

    @Schema(description = "主力撤买(元)")
    private Double mainForcesCb;

    @Schema(description = "主力撤卖(元)")
    private Double mainForcesCs;

    @Schema(description = "主力买入次数")
    private Integer mainForcesBtimes;

    @Schema(description = "主力卖出次数")
    private Integer mainForcesStimes;

    @Schema(description = "买入方向")
    private String direction;

    @Schema(description = "买入体量(元/笔)")
    private Double buyAvgAmount;

    @Schema(description = "卖出体量(元/笔)")
    private Double sellAvgAmount;

    @Schema(description = "数据状态：0无效,1有效", example = "1")
    private Integer status = 1;
}
