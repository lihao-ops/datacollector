package com.hao.datacollector.service;

import com.hao.datacollector.common.cache.DateCache;
import com.hao.datacollector.common.constant.DateTimeFormatConstant;
import com.hao.datacollector.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class LimitUpServiceTest {

    @Autowired
    private LimitUpService limitUpService;
    @Test
    void transferLimitUpDataToDatabase() {

        List<String> currentYearTradeDateList = DateUtil.formatLocalDateList(DateCache.CurrentYearTradeDateList, DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT);
        currentYearTradeDateList.forEach(date -> {
            Boolean success = limitUpService.transferLimitUpDataToDatabase(date);
            if (!success) {
                log.error("Date={} 转移失败", date);
            }
        });
    }
}