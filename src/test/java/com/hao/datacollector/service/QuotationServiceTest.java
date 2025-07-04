package com.hao.datacollector.service;

import com.hao.datacollector.common.cache.DateCache;
import com.hao.datacollector.common.cache.StockCache;
import com.hao.datacollector.common.constant.DateTimeFormatConstant;
import com.hao.datacollector.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class QuotationServiceTest {

    @Autowired
    private QuotationService quotationService;

    @Test
    void transferQuotationBaseByStock() {
        String startDate = DateUtil.formatLocalDate(DateCache.CurrentYearTradeDateList.get(0), DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT);
        String endDate = DateUtil.formatLocalDate(DateCache.CurrentYearTradeDateList.get(DateCache.CurrentYearTradeDateList.size() - 1), DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT);
        for (String windCode : StockCache.allWindCode) {
            Boolean transferResult = quotationService.transferQuotationBaseByStock("600000.SH", "2023-05-01", "2023-05-05");
            log.info("transferQuotationBaseByStock_result={},windCode={},startDate={},endDate={}", transferResult, windCode, startDate, endDate);
        }
    }
}