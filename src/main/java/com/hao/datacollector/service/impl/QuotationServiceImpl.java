package com.hao.datacollector.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hao.datacollector.common.constant.DataSourceConstant;
import com.hao.datacollector.common.constant.DateTimeFormatConstant;
import com.hao.datacollector.common.utils.DateUtil;
import com.hao.datacollector.common.utils.HttpUtil;
import com.hao.datacollector.common.utils.MathUtil;
import com.hao.datacollector.dal.dao.QuotationMapper;
import com.hao.datacollector.dto.table.quotation.QuotationStockBaseDTO;
import com.hao.datacollector.service.QuotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-07-04 17:43:47
 * @description: 行情实现类
 */
@Slf4j
@Service
public class QuotationServiceImpl implements QuotationService {
    @Value("${wind_base.session_id}")
    private String windSessionId;

    @Value("${wind_base.quotation.base.url}")
    private String QuotationBaseUrl;

    @Autowired
    private QuotationMapper quotationMapper;

    /**
     * 获取基础行情数据
     *
     * @param windCode  股票代码
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return 当前股票基础行情数据
     */
    @Override
    public Boolean transferQuotationBaseByStock(String windCode, String startDate, String endDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(DataSourceConstant.WIND_POINT_SESSION_NAME, windSessionId);
        String url = DataSourceConstant.WIND_PROD_WGQ + String.format(QuotationBaseUrl, windCode, startDate, endDate);
        ResponseEntity<String> response = HttpUtil.sendGetRequest(url, headers, 30000, 30000);
        List<List<Long>> quotationList = JSON.parseObject(response.getBody(), new TypeReference<List<List<Long>>>() {
        });
        List<QuotationStockBaseDTO> quotationStockBaseList = new ArrayList<>();
        for (List<Long> quotationData : quotationList) {
            if (quotationData.isEmpty()) {
                log.error("quotationData.isEmpty()!windCode={}", windCode);
                continue;
            }
            QuotationStockBaseDTO quotationStockBaseDTO = new QuotationStockBaseDTO();
            quotationStockBaseDTO.setWindCode(windCode);
            quotationStockBaseDTO.setTradeDate(DateUtil.parseToLocalDate(String.valueOf(quotationData.get(0)), DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT));
            //元
            quotationStockBaseDTO.setOpenPrice(MathUtil.shiftDecimal(quotationData.get(1).toString(), 2));
            //元
            quotationStockBaseDTO.setHighPrice(MathUtil.shiftDecimal(quotationData.get(2).toString(), 2));
            //元
            quotationStockBaseDTO.setLowPrice(MathUtil.shiftDecimal(quotationData.get(3).toString(), 2));
            //手
            quotationStockBaseDTO.setVolume(MathUtil.shiftDecimal(quotationData.get(4).toString(), 2));
            //元
            quotationStockBaseDTO.setAmount(MathUtil.shiftDecimal(quotationData.get(5).toString(), 0));
            //元
            quotationStockBaseDTO.setClosePrice(MathUtil.shiftDecimal(quotationData.get(6).toString(), 2));
            //%
            quotationStockBaseDTO.setTurnoverRate(MathUtil.shiftDecimal(quotationData.get(7).toString(), 2));
            quotationStockBaseList.add(quotationStockBaseDTO);
        }
        if (quotationStockBaseList.isEmpty()) {
            log.error("transferQuotationBaseByStock_list=null!,windCode={}", windCode);
            return false;
        }
        int insertResult = quotationMapper.ins0ertQuotationStockBaseList(quotationStockBaseList);
        return insertResult > 0;
    }
}
