package com.hao.datacollector.service.impl;

import com.hao.datacollector.common.cache.StockCache;
import com.hao.datacollector.common.constant.DateTimeFormatConstant;
import com.hao.datacollector.common.utils.DateUtil;
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
//        String startDate = "20250101";
//        String endDate = DateUtil.stringTimeToAdjust("20250625", DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT, 1);
        String startDate = "20240101";
        String endDate = DateUtil.stringTimeToAdjust("20241231", DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT, 1);
        List<String> jobEndList = announcementMapper.getJobAnnouncementEndWindCodeList(startDate, endDate);
        jobStockList.removeAll(jobEndList);
        //删除异常股票列表
        List<String> abnormalStockList = baseDataMapper.getAbnormalStockList();
        jobStockList.removeAll(abnormalStockList);
        for (String windCode : jobStockList) {
            Boolean transferAnnouncementResult = announcementService.transferAnnouncement(windCode, "20240101", "20241231", 1, 500);
            log.info("AnnouncementServiceImplTest_transferAnnouncement_windCode={},transferAnnouncementResult={}", windCode, transferAnnouncementResult);
        }
    }

    @Test
    void transferEvent() {
        //去除近期已转档过的代码
        List<String> jobStockList = StockCache.allWindCode;
//        String startDate = "20250101";
//        String endDate = DateUtil.stringTimeToAdjust("20250625", DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT, 1);
        String startDate = "20240101";
        String endDate = DateUtil.stringTimeToAdjust("20241231", DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT, 1);
        List<String> jobEndList = announcementMapper.getJobEventEndWindCodeList(startDate, endDate);
        jobStockList.removeAll(jobEndList);
        //删除异常股票列表
        List<String> abnormalStockList = baseDataMapper.getAbnormalStockList();
        jobStockList.removeAll(abnormalStockList);
        for (String windCode : jobStockList) {
            Boolean transferEventResult = announcementService.transferEvent(windCode, "20240101", "20241231", 1, 500);
            log.info("AnnouncementServiceImplTest_transferEvent_windCode={},transferEventResult={}", windCode, transferEventResult);
        }
    }
}