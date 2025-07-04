package com.hao.datacollector.service;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-06-20 17:09:27
 * @description: 行情service
 */
public interface QuotationService {
    /**
     * 获取基础行情数据
     *
     * @param windCode  股票代码
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return 当前股票基础行情数据
     */
    Boolean transferQuotationBaseByStock(String windCode, String startDate, String endDate);
}
