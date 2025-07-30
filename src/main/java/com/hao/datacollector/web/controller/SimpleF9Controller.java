package com.hao.datacollector.web.controller;

import com.hao.datacollector.dto.f9.*;
import com.hao.datacollector.service.SimpleF9Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Hao Li
 * @description: 简版F9
 */
@Tag(name = "简版F9")
@RestController
@RequestMapping("/f9")
public class SimpleF9Controller {

    @Autowired
    private SimpleF9Service simpleF9Service;

    @Operation(summary = "获取公司简介信息数据源")
    @Parameters({
            @Parameter(name = "lan", description = "多语言(默认:CN)", required = false),
            @Parameter(name = "windCode", description = "股票代码", required = true)
    })
    @GetMapping("/get_company_profile_source")
    public CompanyProfileDTO getCompanyProfileSource(@RequestParam(required = false, defaultValue = "CN") String lan, String windCode) {
        return simpleF9Service.getCompanyProfileSource(lan, windCode);
    }

    @Operation(summary = "获取资讯数据源")
    @Parameters({
            @Parameter(name = "lan", description = "多语言(默认:CN)", required = false),
            @Parameter(name = "windCode", description = "股票代码", required = true)
    })
    @GetMapping("/get_information_source")
    public List<InformationOceanDTO> getInformationSource(@RequestParam(required = false, defaultValue = "CN") String lan, String windCode) {
        return simpleF9Service.getInformationSource(lan, windCode);
    }

    @Operation(summary = "获取关键统计数据源")
    @Parameters({
            @Parameter(name = "lan", description = "多语言(默认:CN)", required = false),
            @Parameter(name = "windCode", description = "股票代码", required = true)
    })
    @GetMapping("/get_key_statistics_source")
    public KeyStatisticsDTO getKeyStatisticsSource(@RequestParam(required = false, defaultValue = "CN") String lan, String windCode) {
        return simpleF9Service.getKeyStatisticsSource(lan, windCode);
    }

    @Operation(summary = "获取公司信息数据源")
    @Parameters({
            @Parameter(name = "lan", description = "多语言(默认:CN)", required = false),
            @Parameter(name = "windCode", description = "股票代码", required = true)
    })
    @GetMapping("/get_company_info_source")
    public CompanyInfo getCompanyInfoSource(@RequestParam(required = false, defaultValue = "CN") String lan, String windCode) {
        return simpleF9Service.getCompanyInfoSource(lan, windCode);
    }

    @Operation(summary = "获取公告数据源")
    @Parameters({
            @Parameter(name = "lan", description = "多语言(默认:CN)", required = false),
            @Parameter(name = "windCode", description = "股票代码", required = true)
    })
    @GetMapping("/get_notice_source")
    public List<NoticeDTO> getNoticeSource(@RequestParam(required = false, defaultValue = "CN") String lan, String windCode) {
        return simpleF9Service.getNoticeSource(lan, windCode);
    }

    @Operation(summary = "获取大事数据源")
    @Parameters({
            @Parameter(name = "lan", description = "多语言(默认:CN)", required = false),
            @Parameter(name = "windCode", description = "股票代码", required = true)
    })
    @GetMapping("/get_great_event_source")
    public List<GreatEventDTO> getGreatEventSource(@RequestParam(required = false, defaultValue = "CN") String lan, String windCode) {
        return simpleF9Service.getGreatEventSource(lan, windCode);
    }

    @Operation(summary = "获取盈利预测数据源")
    @Parameters({
            @Parameter(name = "lan", description = "多语言(默认:CN)", required = false),
            @Parameter(name = "windCode", description = "股票代码", required = true)
    })
    @GetMapping("/get_profit_forecast_source")
    public ProfitForecastDTO getProfitForecastSource(@RequestParam(required = false, defaultValue = "CN") String lan, String windCode) {
        return simpleF9Service.getProfitForecastSource(lan, windCode);
    }

    @Operation(summary = "获取市场表现数据源")
    @Parameters({
            @Parameter(name = "lan", description = "多语言(默认:CN)", required = false),
            @Parameter(name = "windCode", description = "股票代码", required = true)
    })
    @GetMapping("/get_market_performance_source")
    public MarketPerformanceDTO getMarketPerformanceSource(@RequestParam(required = false, defaultValue = "CN") String lan, String windCode) {
        return simpleF9Service.getMarketPerformanceSource(lan, windCode);
    }

    @Operation(summary = "获取PE_BAND数据源")
    @Parameters({
            @Parameter(name = "lan", description = "多语言(默认:CN)", required = false),
            @Parameter(name = "windCode", description = "股票代码", required = true)
    })
    @GetMapping("/get_pe_band_source")
    public List<PeBandVO> getPeBandSource(@RequestParam(required = false, defaultValue = "CN") String lan, String windCode) {
        return simpleF9Service.getPeBandSource(lan, windCode);
    }

    @Operation(summary = "获取估值指标数据源")
    @Parameters({
            @Parameter(name = "lan", description = "多语言(默认:CN)", required = false),
            @Parameter(name = "windCode", description = "股票代码", required = true)
    })
    @GetMapping("/get_security_margin_source")
    public List<ValuationIndexDTO> getSecurityMarginSource(@RequestParam(required = false, defaultValue = "CN") String lan, String windCode) {
        return simpleF9Service.getSecurityMarginSource(lan, windCode);
    }

    @Operation(summary = "获取成长能力数据源")
    @Parameters({
            @Parameter(name = "lan", description = "多语言(默认:CN)", required = false),
            @Parameter(name = "windCode", description = "股票代码", required = true)
    })
    @GetMapping("/get_financial_summary_source")
    public List<QuickViewGrowthDTO> getFinancialSummarySource(@RequestParam(required = false, defaultValue = "CN") String lan, String windCode) {
        return simpleF9Service.getFinancialSummarySource(lan, windCode);
    }
}