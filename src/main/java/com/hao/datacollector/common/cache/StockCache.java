package com.hao.datacollector.common.cache;

import com.hao.datacollector.dal.dao.BaseDataMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-06-20 21:11:07
 * @description: 股票相关缓存
 */
@Slf4j
@Component("StockCache")
public class StockCache {

    @Autowired
    private BaseDataMapper baseDataMapper;

    /**
     * 全部A股代码
     */
    public static List<String> allWindCode;

    @PostConstruct
    private void initDateList() {
        // 获取所有A股的代码
        allWindCode = baseDataMapper.getAllAStockCode();
        log.info("StockCache_allWindCode.size={}", allWindCode.size());
    }
}
