package com.hao.datacollector.web.vo.limitup;

import lombok.Data;

@Data
//@ApiModel("股票主题VO对象")
//extends BaseVO
public class TopicStockVO {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    //@ApiModelProperty(value = "股票代码 1", required = true)
    private String windCode;

    //@ApiModelProperty(value = "股票名称 2", required = true)
    private String name;

    //@ApiModelProperty(value = "首次涨停时间 3", required = true)
    private String firstTime;

    //@ApiModelProperty(value = "连板情况，几天几板 4", required = true)
    private String status;

    //@ApiModelProperty(value = "涨停原因 关联的热度最高的两个标签id 5", required = true)
    private TopicInfoVO[] topics;

    //@ApiModelProperty(value = "涨停封单额 6", required = true)
    private Double orderTotal;

    //@ApiModelProperty(value = "主力流入 7", required = true)
    private Double volumeNetin;

    //@ApiModelProperty(value = "流通市值 8", required = true)
    private Double listedStock;

    //@ApiModelProperty(value = "当前价格 9", required = true)
    private Double price;

    //@ApiModelProperty(value = "换手率 10", required = true)
    private Double turnoverRate;

    //@ApiModelProperty(value = "开板时间 11", required = true)
    private String openTime;

    //@ApiModelProperty(value = "开板次数 12", required = true)
    private Integer openTimes;

    //@ApiModelProperty(value = "涨幅 13", required = true)
    private Double changeRange;

    //@ApiModelProperty(value = "时间(HHMMSSmmm)", required = true)
    private Integer time;

    //@ApiModelProperty(value = "5分钟涨速", required = true)
    private Double changeRange5;

    //@ApiModelProperty(value = "最高5分钟涨速", required = true)
    private Double changeRange5Max;

    //@ApiModelProperty(value = "1分钟涨速", required = true)
    private Double changeRange1;

    //@ApiModelProperty(value = "最高1分钟涨速", required = true)
    private Double changeRange1Max;

    //@ApiModelProperty(value = "竞价开板 涨停价委托买", required = true)
    private Double callAuctionOrderTotal;

    /**
     * 连扳情况X系数,
     * x系数意思为第x天前有涨停，0-5之间的整数
     * 1）x=0，则代表前一天有涨停，显示N连板或N天M板
     * 2）0<x<=4,则代表前x天有涨停，显示x天前N连板或x天前N天M板
     * 3）x=5，则代表前五天均没有涨停，则显示未涨停过
     */
    //@ApiModelProperty(value = "连扳情况X系数", required = true)
    private Integer limitUpX;

    //@ApiModelProperty(value = "连扳情况N系数,若N=M，则N连板，若N>M，则N天M板", required = true)
    private Integer limitUpN;

    //@ApiModelProperty(value = "连扳情况M系数,若N=M，则N连板，若N>M，则N天M板", required = true)
    private Integer limitUpM;

    //@ApiModelProperty(value = "计算标签热度和", required = true)
    private Double topicHotSum;

    //@ApiModelProperty(value = "低位涨幅", required = true)
    private Double lowRange;

    //@ApiModelProperty(value = "主力强度", required = true)
    private Double mainForces;

    //@ApiModelProperty(value = "主力成本", required = true)
    private Double cost;

    //@ApiModelProperty(value = "主力浮盈", required = true)
    private Double profit;

    //@ApiModelProperty(value = "主力流入", required = true)
    private Double mainForcesIn;

    //@ApiModelProperty(value = "主力分歧", required = true)
    private Double divergency;

    //@ApiModelProperty(value = "主力时间", required = true)
    private Integer mainForcesTime;

    //@ApiModelProperty(value = "主力撤买", required = true)
    private Double mainForcesCB;

    //@ApiModelProperty(value = "主力撤卖", required = true)
    private Double mainForcesCS;

    //@ApiModelProperty(value = "主力买入（次)", required = true)
    private Integer mainForcesBtimes;

    //@ApiModelProperty(value = "主力卖出（次）", required = true)
    private Integer mainForcesStimes;

    //@ApiModelProperty(value = "买入方向", required = true)
    private String direction;

    //@ApiModelProperty(value = "买入体量", required = true)
    private double buyAvgAmount;

    //@ApiModelProperty(value = "卖出体量", required = true)
    private double sellAvgAmount;

}