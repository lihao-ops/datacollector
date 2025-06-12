package com.hao.datacollector.service;

import com.hao.datacollector.web.vo.limitup.LimitResultVO;
import com.hao.datacollector.web.vo.limitup.TopicStockInfoResultVO;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-06-12 19:34:09
 * @description: 涨停相关service
 */
public interface LimitUpService {
    
    /**
     * 获取涨停转档数据
     * @return 涨停转档数据结果
     */
    LimitResultVO<TopicStockInfoResultVO> getLimitUpData();
    
    /**
     * 获取涨停转档数据并转档到MySQL数据库
     * @param dateTime 查询日期，格式：yyyyMMdd
     * @return 转档结果信息
     */
    String transferLimitUpDataToDatabase(String dateTime);
}
