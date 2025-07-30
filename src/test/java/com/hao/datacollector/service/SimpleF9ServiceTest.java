package com.hao.datacollector.service;

import com.alibaba.fastjson.JSON;
import com.hao.datacollector.dto.f9.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Hao Li
 * @description:
 */
@Slf4j
@SpringBootTest
public class SimpleF9ServiceTest {
    @Autowired
    private SimpleF9Service simpleF9Service;

    @Test
    void getCompanyProfileSource() {
        CompanyProfileDTO companyProfile = simpleF9Service.getCompanyProfileSource("cn", "600519.SH");
        log.info(JSON.toJSONString(companyProfile));
    }

    @Test
    void getInformationSource() {
        List<InformationOceanDTO> information = simpleF9Service.getInformationSource("cn", "600519.SH");
        log.info(JSON.toJSONString(information));
    }

    @Test
    void getKeyStatisticsSource() {
        KeyStatisticsDTO keyStatistics = simpleF9Service.getKeyStatisticsSource("cn", "600519.SH");
        log.info(JSON.toJSONString(keyStatistics));
    }

    @Test
    void getCompanyInfoSource() {
        CompanyInfo companyInfo = simpleF9Service.getCompanyInfoSource("cn", "600519.SH");
        log.info(JSON.toJSONString(companyInfo));
    }

    @Test
    void getNoticeSource() {
        List<NoticeDTO> notice = simpleF9Service.getNoticeSource("cn", "600519.SH");
        log.info(JSON.toJSONString(notice));
    }

    @Test
    void getGreatEventSource() {
        List<GreatEventDTO> greatEvent = simpleF9Service.getGreatEventSource("cn", "600519.SH");
        log.info(JSON.toJSONString(greatEvent));
    }

    @Test
    void getProfitForecastSource() {
        ProfitForecastDTO profitForecast = simpleF9Service.getProfitForecastSource("cn", "600519.SH");
        log.info(JSON.toJSONString(profitForecast));
    }

    @Test
    void getMarketPerformanceSource() {
        MarketPerformanceDTO marketPerformance = simpleF9Service.getMarketPerformanceSource("cn", "600519.SH");
        log.info(JSON.toJSONString(marketPerformance));
    }

    @Test
    void getPeBandSource() {
        List<PeBandVO> peBand = simpleF9Service.getPeBandSource("cn", "600519.SH");
        log.info(JSON.toJSONString(peBand));
    }

    @Test
    void getSecurityMarginSource() {
        List<ValuationIndexDTO> securityMargin = simpleF9Service.getSecurityMarginSource("cn", "600519.SH");
        log.info(JSON.toJSONString(securityMargin));
    }

    @Test
    void getFinancialSummarySource() {
        List<QuickViewGrowthDTO> quickViewGrowthCapability = simpleF9Service.getFinancialSummarySource("cn", "600519.SH");
        log.info(JSON.toJSONString(quickViewGrowthCapability));
    }
}