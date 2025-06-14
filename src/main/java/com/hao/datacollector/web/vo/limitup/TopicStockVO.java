package com.hao.datacollector.web.vo.limitup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "主题下股票VO对象")
public class TopicStockVO {
    @Schema(description = "股票代码 1", required = true)
    private String windCode;

    @Schema(description = "股票名称 2", required = true)
    private String name;

    @Schema(description = "首次涨停时间 3", required = true)
    private String firstTime;

    @Schema(description = "连板情况，几天几板 4", required = true)
    private String status;

    @Schema(description = "涨停原因 关联的热度最高的两个标签id 5", required = true)
    private TopicInfoVO[] topics;

    @Schema(description = "涨停封单额 6", required = true)
    private Double orderTotal;

    @Schema(description = "主力流入 7", required = true)
    private Double volumeNetin;

    @Schema(description = "流通市值 8", required = true)
    private Double listedStock;

    @Schema(description = "当前价格 9", required = true)
    private Double price;

    @Schema(description = "换手率 10", required = true)
    private Double turnoverRate;

    @Schema(description = "开板时间 11", required = true)
    private String openTime;

    @Schema(description = "开板次数 12", required = true)
    private Integer openTimes;

    @Schema(description = "涨幅 13", required = true)
    private Double changeRange;

    @Schema(description = "时间(HHMMSSmmm)", required = true)
    private Integer time;

    @Schema(description = "5分钟涨速", required = true)
    private Double changeRange5;

    @Schema(description = "最高5分钟涨速", required = true)
    private Double changeRange5Max;

    @Schema(description = "1分钟涨速", required = true)
    private Double changeRange1;

    @Schema(description = "最高1分钟涨速", required = true)
    private Double changeRange1Max;

    @Schema(description = "竞价开板 涨停价委托买", required = true)
    private Double callAuctionOrderTotal;

    @Schema(description = "连扳情况X系数,x系数意思为第x天前有涨停，0-5之间的整数。1）x=0，则代表前一天有涨停，显示N连板或N天M板。2）0<x<=4,则代表前x天有涨停，显示x天前N连板或x天前N天M板。3）x=5，则代表前五天均没有涨停，则显示未涨停过", required = true)
    private Integer limitUpX;

    @Schema(description = "连扳情况N系数,若N=M，则N连板，若N>M，则N天M板", required = true)
    private Integer limitUpN;

    @Schema(description = "连扳情况M系数,若N=M，则N连板，若N>M，则N天M板", required = true)
    private Integer limitUpM;

    @Schema(description = "计算标签热度和", required = true)
    private Double topicHotSum;

    @Schema(description = "低位涨幅", required = true)
    private Double lowRange;

    @Schema(description = "主力强度", required = true)
    private Double mainForces;

    @Schema(description = "主力成本", required = true)
    private Double cost;

    @Schema(description = "主力浮盈", required = true)
    private Double profit;

    @Schema(description = "主力流入", required = true)
    private Double mainForcesIn;

    @Schema(description = "主力分歧", required = true)
    private Double divergency;

    @Schema(description = "主力时间", required = true)
    private Integer mainForcesTime;

    @Schema(description = "主力撤买", required = true)
    private Double mainForcesCB;

    @Schema(description = "主力撤卖", required = true)
    private Double mainForcesCS;

    @Schema(description = "主力买入（次)", required = true)
    private Integer mainForcesBtimes;

    @Schema(description = "主力卖出（次）", required = true)
    private Integer mainForcesStimes;

    @Schema(description = "买入方向", required = true)
    private String direction;

    @Schema(description = "买入体量", required = true)
    private double buyAvgAmount;

    @Schema(description = "卖出体量", required = true)
    private double sellAvgAmount;

}