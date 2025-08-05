package com.hao.datacollector.common.cache;

import com.hao.datacollector.common.constant.DateTimeFormatConstants;
import com.hao.datacollector.common.utils.DateUtil;
import com.hao.datacollector.dto.param.limitup.LimitUpStockQueryParam;
import com.hao.datacollector.service.LimitUpService;
import com.hao.datacollector.web.vo.limitup.LimitUpStockQueryResultVO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-08-05 18:49:36
 * @description: 涨停相关缓存
 */
@Slf4j
@Component("LimitCache")
public class LimitCache {
    @Autowired
    private LimitUpService limitUpService;

    /**
     * 涨停股票映射股票Set
     * key:交易日期,value:当天涨停股票代码Set
     */
    public static Map<String, Set<String>> limitUpMappingStockMap = new HashMap<>();
    //todo 需优化 加载太慢
//    @PostConstruct
//    public void initLimitUpMappingStockCache() {
//        List<LimitUpStockQueryResultVO> upStockQueryResultVOList = limitUpService.queryLimitUpStockList(new LimitUpStockQueryParam());
//        Map<String, Set<String>> limitUpMappingStockMap = new HashMap<>();
//        for (LimitUpStockQueryResultVO vo : upStockQueryResultVOList) {
//            String tradeDate = DateUtil.formatLocalDate(vo.getTradeDate(), DateTimeFormatConstants.DEFAULT_DATE_FORMAT);
//            limitUpMappingStockMap.computeIfAbsent(tradeDate, k -> new HashSet<>())
//                    .add(vo.getWindCode());
//        }
//        LimitCache.limitUpMappingStockMap = limitUpMappingStockMap;
//        log.info("LimitCache_initLimitUpMappingStockCache_success,limitUpMappingStockMap.size={}", limitUpMappingStockMap.size());
//    }
}
