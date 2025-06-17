package com.hao.datacollector.dal.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LimitUpMapper {

    /**
     * 批量插入每日股票功能数量汇总数据
     * @param dailyStockSummaryList 每日股票功能数量汇总数据列表
     * @return 影响行数
     */
    int batchInsertDailyStockSummary(@Param("list") List<DailyStockSummaryInsertDTO> dailyStockSummaryList);

    /**
     * 批量插入每日涨停股票明细数据
     * @param limitUpStockDailyList 每日涨停股票明细数据列表
     * @return 影响行数
     */
    int batchInsertLimitUpStockDaily(@Param("list") List<LimitUpStockDailyInsertDTO> limitUpStockDailyList);

    /**
     * 批量插入股票标签关联数据
     * @param stockTopicRelationList 股票标签关联数据列表
     * @return 影响行数
     */
    int batchInsertStockTopicRelation(@Param("list") List<StockTopicRelationInsertDTO> stockTopicRelationList);

    /**
     * 根据交易日和功能ID删除每日股票功能数量汇总数据
     * @param tradeDate 交易日
     * @param functionId 功能ID
     */
    void deleteDailyStockSummaryByTradeDateAndFunctionId(@Param("tradeDate") String tradeDate, @Param("functionId") String functionId);

    /**
     * 根据交易日删除每日涨停股票明细数据
     * @param tradeDate 交易日
     */
    void deleteLimitUpStockDailyByTradeDate(@Param("tradeDate") String tradeDate);

    /**
     * 根据交易日删除股票标签关联数据
     * @param tradeDate 交易日
     */
    void deleteStockTopicRelationByTradeDate(@Param("tradeDate") String tradeDate);
}
