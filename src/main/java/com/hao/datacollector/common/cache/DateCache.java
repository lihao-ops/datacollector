package com.hao.datacollector.common.cache;

import com.alibaba.fastjson.JSON;
import com.hao.datacollector.common.constant.DateTimeFormatConstant;
import com.hao.datacollector.common.utils.DateUtil;
import com.hao.datacollector.service.BaseDataService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-06-14 17:34:21
 * @description: 日期相关数据缓存
 */
@Slf4j
@Component("DateCache")
public class DateCache {
    /**
     * 今年整年交易日历
     */
    public static List<LocalDate> ThisYearTradeDateList;

    /**
     * 年初至今的交易日历
     */
    public static List<LocalDate> CurrentYearTradeDateList;

    @Autowired
    private BaseDataService baseDataService;

    @PostConstruct
    private void initDateList() {
        //今年整年的交易日历
        String firstDayOfYear = DateUtil.getFirstDayOfYear(DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT);
        String lastDayOfYear = DateUtil.getLastDayOfYear(DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT);
        ThisYearTradeDateList = baseDataService.getTradeDateListByTime(firstDayOfYear, lastDayOfYear);
        //年初至今的交易日历
        String currentDay = DateUtil.getCurrentDateTime(DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT);
        CurrentYearTradeDateList = baseDataService.getTradeDateListByTime(firstDayOfYear, currentDay);
        log.info("DateCache_CurrentYearTradeDateList.size={},CurrentYearTradeDateList.size={}", ThisYearTradeDateList.size(), JSON.toJSONString(CurrentYearTradeDateList));
    }
}
