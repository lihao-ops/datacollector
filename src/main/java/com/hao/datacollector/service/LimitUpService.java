package com.hao.datacollector.service;

import com.hao.datacollector.web.vo.limitup.ApiResponse;
import com.hao.datacollector.web.vo.limitup.ResultObjectVO;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-06-12 19:34:31
 * @description: 涨停相关接口
 */
public interface LimitUpService {

    /**
     * 获取解析后的涨停选股接口数据
     *
     * @param tradeTime 交易日期
     * @return 解析结果对象
     */
    ApiResponse<ResultObjectVO> getLimitUpData(String tradeTime);

    /**
     * 将涨停数据转档到数据库
     *
     * @param tradeTime 交易日期
     * @return 是否成功
     */
    Boolean transferLimitUpDataToDatabase(String tradeTime);
}
