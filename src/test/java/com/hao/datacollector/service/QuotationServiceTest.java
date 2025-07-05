package com.hao.datacollector.service;

import com.hao.datacollector.common.cache.StockCache;
import com.hao.datacollector.common.constant.DateTimeFormatConstant;
import com.hao.datacollector.common.utils.DateUtil;
import com.hao.datacollector.dal.dao.QuotationMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
class QuotationServiceTest {

    @Autowired
    private QuotationService quotationService;
    @Autowired
    private QuotationMapper quotationMapper;

    @Test
    void transferQuotationBaseByStock() {
        List<String> allWindCodeList = new ArrayList<>(StockCache.allWindCode);
//        String startDate = DateUtil.formatLocalDate(DateCache.CurrentYearTradeDateList.get(0), DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT);
        String startDate = "20200101";
        String endDate = DateUtil.stringTimeToAdjust(DateUtil.getCurrentDateTimeByStr(DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT), DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT, 1);
        //需要剔除已经转档的股票
        List<String> endWindCodeList = quotationMapper.getJobQuotationBaseEndWindCodeList(startDate, endDate);
        allWindCodeList.removeAll(endWindCodeList);
        for (String windCode : allWindCodeList) {
            Boolean transferResult = quotationService.transferQuotationBaseByStock(windCode, startDate, endDate);
            log.info("transferQuotationBaseByStock_result={},windCode={},startDate={},endDate={}", transferResult, windCode, startDate, endDate);
        }
    }
}