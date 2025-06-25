package com.hao.datacollector.service.impl;

import com.hao.datacollector.common.cache.StockCache;
import com.hao.datacollector.dal.dao.AnnouncementMapper;
import com.hao.datacollector.dal.dao.BaseDataMapper;
import com.hao.datacollector.service.AnnouncementService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class AnnouncementServiceImplTest {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private BaseDataMapper baseDataMapper;

    @Test
    void transferAnnouncement() {
        //去除近期已转档过的代码
        List<String> jobStockList = StockCache.allWindCode;
        //todo 解决查询数据缺失问题
        List<String> jobEndList = announcementMapper.getJobAnnouncementEndWindCodeList("20250625");
        jobStockList.removeAll(jobEndList);
        //删除异常股票列表
        List<String> abnormalStockList = baseDataMapper.getAbnormalStockList();
        jobStockList.removeAll(abnormalStockList);
        for (String windCode : jobStockList) {
            Boolean transferAnnouncementResult = announcementService.transferAnnouncement(windCode, "20250101", "20250625", 1, 500);
            log.info("AnnouncementServiceImplTest_transferAnnouncement_windCode={},transferAnnouncementResult={}", windCode, transferAnnouncementResult);
        }
    }

    @Test
    void transferEvent() {
        //去除近期已转档过的代码
        List<String> jobStockList = StockCache.allWindCode;
        //todo 解决查询数据缺失问题
        List<String> jobEndList = announcementMapper.getJobEventEndWindCodeList("20250625");
        jobStockList.removeAll(jobEndList);
        //删除异常股票列表
        List<String> abnormalStockList = baseDataMapper.getAbnormalStockList();
        jobStockList.removeAll(abnormalStockList);
        for (String windCode : jobStockList) {
            Boolean transferEventResult = announcementService.transferEvent(windCode, "20250101", "20250625", 1, 500);
            log.info("AnnouncementServiceImplTest_transferEvent_windCode={},transferEventResult={}", windCode, transferEventResult);
        }
    }
}