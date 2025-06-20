package com.hao.datacollector.service;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-06-20 17:09:27
 * @description: 新闻相关service
 */
public interface NewsService {

    /**
     * 转档股票新闻数据
     *
     * @param windCode 股票代码
     * @return 操作结果
     */
    Boolean transferNewsStockData(String windCode);
}
