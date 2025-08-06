package com.hao.datacollector.common.cache;

import com.hao.datacollector.service.LimitUpService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
    public static Map<String, Set<String>> limitUpMappingStockMap = new TreeMap<>();

    @PostConstruct
    public void initLimitUpMappingStockCache() {
        LimitCache.limitUpMappingStockMap = limitUpService.getLimitUpTradeDateMap(null, null);
        log.info("LimitCache_initLimitUpMappingStockCache_success,limitUpMappingStockMap.size={}", limitUpMappingStockMap.size());
    }
}
