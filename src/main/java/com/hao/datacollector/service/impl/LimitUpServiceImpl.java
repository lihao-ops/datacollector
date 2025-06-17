package com.hao.datacollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.datacollector.common.cache.DateCache;
import com.hao.datacollector.common.constant.DataSourceConstant;
import com.hao.datacollector.common.constant.DateTimeFormatConstant;
import com.hao.datacollector.common.utils.DateUtil;
import com.hao.datacollector.common.utils.HttpUtil;
import com.hao.datacollector.dal.dao.LimitUpMapper;
import com.hao.datacollector.dto.table.limitup.BaseTopicInsertDTO;
import com.hao.datacollector.dto.table.limitup.LimitUpStockInfoInsertDTO;
import com.hao.datacollector.dto.table.limitup.LimitUpStockTopicRelationInsertDTO;
import com.hao.datacollector.service.LimitUpService;
import com.hao.datacollector.web.vo.limitup.ApiResponse;
import com.hao.datacollector.web.vo.limitup.ResultObjectVO;
import com.hao.datacollector.web.vo.limitup.TopicInfoVO;
import com.hao.datacollector.web.vo.limitup.TopicStockVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-06-12 19:34:31
 * @description: 涨停相关接口实现类
 */
@Slf4j
@Service
public class LimitUpServiceImpl implements LimitUpService {

    @Autowired
    private LimitUpMapper limitUpMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${wind_base.limit_up.url}") // Corrected @Value annotation
    private String limitUpBaseUrl;

    @Value("${wind_base.session_id}")
    private String windSessionId;

    /**
     * 获取解析后的涨停选股接口数据
     *
     * @param tradeTime 交易日期
     * @return 解析结果对象
     */
    @Override
    public ApiResponse<ResultObjectVO> getLimitUpData(String tradeTime) { // Changed return type
        try {
            if (!StringUtils.hasLength(tradeTime)) {
                tradeTime = DateUtil.getCurrentDateTime(DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT);
            }
            //非交易日无数据。
            // Corrected DateCache usage if it was incorrect
            if (!DateCache.ThisYearTradeDateList.contains(DateUtil.parseToLocalDate(tradeTime, DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT))) {
                log.error("LimitUpServiceImpl_getLimitUpData: {} is not a trade date.", tradeTime);
                throw new RuntimeException("LimitUpServiceImpl_getLimitUpData: " + tradeTime + " is not a trade date.");
            }
            //url具体参数含义可查看TopicDetailParam,日期格式必须类似20250609
            String url = String.format(limitUpBaseUrl, tradeTime);
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set(DataSourceConstant.WIND_SESSION_NAME, windSessionId);
            String response = HttpUtil.sendGetRequest(DataSourceConstant.WIND_PROD_WGQ + url, headers, 10000, 30000).getBody();
            if (!StringUtils.hasLength(response)) {
                log.error("LimitUpServiceImpl_getLimitUpData: HTTP response body is empty for tradeTime: {}", tradeTime);
                throw new RuntimeException("LimitUpServiceImpl_getLimitUpData: HTTP response body is empty for tradeTime: " + tradeTime);
            }
            // 配置忽略未知字段，避免反序列化错误。解析JSON响应为ApiResponse对象
            objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ApiResponse<ResultObjectVO> result = objectMapper.readValue(
                    response,
                    objectMapper.getTypeFactory().constructParametricType(
                            ApiResponse.class,
                            ResultObjectVO.class
                    )
            );
            log.info("LimitUpServiceImpl_getLimitUpData,resultCode={}", result.getResultCode());
            if (result == null || !"200".equals(result.getResultCode()) || result.getResultObject() == null || result.getResultObject().getStockDetail() == null || result.getResultObject().getStockDetail().isEmpty()) {
                throw new RuntimeException("获取涨停数据为空或接口返回错误，交易日期: {}，响应码: {}" + tradeTime + result != null ? result.getResultCode() : "null");
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get or parse limit up data:" + e.getMessage());
        }
    }

    /**
     * 涨停数据转档
     *
     * @param tradeTime 交易日期
     * @return 操作结果
     */
    @Override
    public Boolean transferLimitUpDataToDatabase(String tradeTime) {
        //如果tradeTime为空，则使用当前时间
        if (!StringUtils.hasLength(tradeTime)) {
            tradeTime = DateUtil.getCurrentDateTime(DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT);
        }
        try {
            log.info("开始执行涨停数据转档任务，交易日期: {}", tradeTime);
            // 获取涨停数据
            ApiResponse<ResultObjectVO> limitUpResponse = getLimitUpData(tradeTime);
            // 获取详情数据,进行分表转换
            ResultObjectVO resultData = limitUpResponse.getResultObject();
            List<TopicStockVO> stockDetails = resultData.getStockDetail();
            log.info("获取到 {} 条涨停股票数据，开始转档到数据库", stockDetails.size());
            List<LimitUpStockInfoInsertDTO> limitUpStockInfoList = new ArrayList<>();
            //对应基础标签表
            List<BaseTopicInsertDTO> baseTopicInsertList = new ArrayList<>();
            List<LimitUpStockTopicRelationInsertDTO> relationInsertList = new ArrayList<>();
            LocalDate localTradeTime = DateUtil.parseToLocalDate(tradeTime, DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT);
            for (TopicStockVO stockDetail : stockDetails) {
                for (TopicInfoVO stockDetailTopic : stockDetail.getTopics()) {
                    //基础标签
                    BaseTopicInsertDTO baseTopicInsertDTO = new BaseTopicInsertDTO();
                    BeanUtils.copyProperties(stockDetailTopic, baseTopicInsertDTO);
                    baseTopicInsertList.add(baseTopicInsertDTO);
                    //关联标签
                    LimitUpStockTopicRelationInsertDTO relationInsertDTO = new LimitUpStockTopicRelationInsertDTO();
                    relationInsertDTO.setWindCode(stockDetail.getWindCode());
                    relationInsertDTO.setTradeDate(localTradeTime);
                    BeanUtils.copyProperties(stockDetailTopic, relationInsertDTO);
                    relationInsertList.add(relationInsertDTO);
                }
                LimitUpStockInfoInsertDTO stockInfoInsertDTO = new LimitUpStockInfoInsertDTO();
                BeanUtils.copyProperties(stockDetail, stockInfoInsertDTO);
                limitUpStockInfoList.add(stockInfoInsertDTO);
            }
            // 先删除当天旧数据
            log.info("开始删除当前交易日 {} 的旧数据", tradeTime);
            limitUpMapper.deleteLimitUpStockInfoByTradeDate(tradeTime);
            limitUpMapper.deleteStockTopicRelationByTradeDate(tradeTime);
            log.info("删除交易日 {} 的旧数据完成", tradeTime);
            // 批量插入新数据
            log.info("开始批量插入新数据");
            if (!baseTopicInsertList.isEmpty()) {
                //todo 还有去重

                for (BaseTopicInsertDTO topicInsertDTO : baseTopicInsertList) {
                    Boolean insertBaseTopicResult = limitUpMapper.insertBaseTopic(topicInsertDTO);
                    log.info("LimitUpServiceImpl_transferLimitUpDataToDatabase_insertBaseTopicResult={}", insertBaseTopicResult);
                }
                log.info("插入BaseTopic{} 条", baseTopicInsertList.size());
            }
            if (!relationInsertList.isEmpty()) {
                limitUpMapper.batchInsertStockTopicRelation(relationInsertList);
                log.info("插入relationInsertList{} 条", relationInsertList.size());
            }
            if (!limitUpStockInfoList.isEmpty()) {
                limitUpMapper.batchInsertLimitUpStockInfo(limitUpStockInfoList);
                log.info("插入limitUpStockInfoList{} 条", limitUpStockInfoList.size());
            }
            log.info("批量插入新数据完成");
            log.info("涨停数据转档成功，交易日期: {}，共处理 {} 条记录", tradeTime, stockDetails.size());
            return true;
        } catch (Exception e) {
            log.error("涨停数据转档失败，交易日期: {}", tradeTime, e);
            return false;
        }
    }
}
