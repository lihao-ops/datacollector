package com.hao.datacollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.datacollector.common.cache.DateCache;
import com.hao.datacollector.common.constant.DataSourceConstant;
import com.hao.datacollector.common.constant.DateTimeFormatConstant;
import com.hao.datacollector.common.utils.DateUtil;
import com.hao.datacollector.common.utils.HttpUtil;
import com.hao.datacollector.dal.dao.LimitUpMapper;
// Corrected DTO import paths if they were incorrect before, assuming they are in dto.insert
import com.hao.datacollector.dto.insert.DailyStockSummaryInsertDTO;
import com.hao.datacollector.dto.insert.LimitUpStockDailyInsertDTO;
import com.hao.datacollector.dto.insert.StockTopicRelationInsertDTO;
import com.hao.datacollector.service.LimitUpService;
import com.hao.datacollector.web.vo.limitup.ApiResponse;
import com.hao.datacollector.web.vo.limitup.ResultObjectVO;
import com.hao.datacollector.web.vo.limitup.TopicInfoVO;
import com.hao.datacollector.web.vo.limitup.TopicStockVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
                log.warn("LimitUpServiceImpl_getLimitUpData: {} is not a trade date.", tradeTime);
                // Return a meaningful error response
                ApiResponse<ResultObjectVO> errorResponse = new ApiResponse<>();
                errorResponse.setResultCode("201"); // Or some other error code
                // errorResponse.setMessage("Not a trading day"); // ApiResponse doesn't have setMessage
                return errorResponse;
            }
            //url具体参数含义可查看TopicDetailParam,日期格式必须类似20250609
            String url = String.format(limitUpBaseUrl, tradeTime);
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set(DataSourceConstant.WIND_SESSION_NAME, windSessionId);
            String response = HttpUtil.sendGetRequest(DataSourceConstant.WIND_PROD_WGQ + url, headers, 10000, 30000).getBody();

            if (!StringUtils.hasLength(response)){
                log.error("LimitUpServiceImpl_getLimitUpData: HTTP response body is empty for tradeTime: {}", tradeTime);
                ApiResponse<ResultObjectVO> errorResponse = new ApiResponse<>();
                errorResponse.setResultCode("500");
                return errorResponse;
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
            return result;
        } catch (Exception e) {
            log.error("LimitUpServiceImpl_getLimitUpData failed for tradeTime: {}", tradeTime, e);
            // Return a meaningful error response in case of exception
            ApiResponse<ResultObjectVO> errorResponse = new ApiResponse<>();
            errorResponse.setResultCode("500"); // Or some other error code indicating failure
            // errorResponse.setMessage("Failed to get or parse limit up data: " + e.getMessage());
            return errorResponse;
        }
    }

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
            if (limitUpResponse == null || !"200".equals(limitUpResponse.getResultCode()) || limitUpResponse.getResultObject() == null || limitUpResponse.getResultObject().getStockDetail() == null || limitUpResponse.getResultObject().getStockDetail().isEmpty()) {
                log.warn("获取涨停数据为空或接口返回错误，交易日期: {}，响应码: {}", tradeTime, limitUpResponse != null ? limitUpResponse.getResultCode() : "null");
                return false;
            }
            // 获取详情数据,进行分表转换
            ResultObjectVO resultData = limitUpResponse.getResultObject();
            List<TopicStockVO> stockDetails = resultData.getStockDetail();
            // String topic = resultData.getTopic(); // ResultObjectVO does not have getTopic directly
            // int topicId = resultData.getTopicId(); // ResultObjectVO does not have getTopicId directly
            log.info("获取到 {} 条涨停股票数据，开始转档到数据库", stockDetails.size());
            // 1. 准备 DailyStockSummaryInsertDTO 数据
            DailyStockSummaryInsertDTO summaryInsertDTO = new DailyStockSummaryInsertDTO();
            summaryInsertDTO.setTradeDate(tradeTime);
            summaryInsertDTO.setFunctionId("limit_up_stocks"); // 定义一个功能ID，例如 "limit_up_stocks"
            summaryInsertDTO.setStockNum(stockDetails.size());
            summaryInsertDTO.setStatus(1); // 假设1为有效
            List<DailyStockSummaryInsertDTO> dailyStockSummaryList = new ArrayList<>();
            dailyStockSummaryList.add(summaryInsertDTO);
            // 2. 准备 LimitUpStockDailyInsertDTO 列表数据
            List<LimitUpStockDailyInsertDTO> limitUpStockDailyList = new ArrayList<>();
            // 3. 准备 StockTopicRelationInsertDTO 列表数据
            List<StockTopicRelationInsertDTO> stockTopicRelationList = new ArrayList<>();
            for (TopicStockVO stockDetail : stockDetails) {
                // 转换 LimitUpStockDailyInsertDTO
                LimitUpStockDailyInsertDTO limitUpStockDailyDTO = new LimitUpStockDailyInsertDTO();
                limitUpStockDailyDTO.setTradeDate(tradeTime);
                limitUpStockDailyDTO.setWindCode(stockDetail.getWindCode());
                limitUpStockDailyDTO.setName(stockDetail.getName());
                limitUpStockDailyDTO.setFirstTime(stockDetail.getFirstTime());
                limitUpStockDailyDTO.setStatusDesc(stockDetail.getStatus());
                limitUpStockDailyDTO.setOrderTotal(stockDetail.getOrderTotal());
                // Corrected field mapping based on TopicStockVO
                limitUpStockDailyDTO.setMainForcesIn(stockDetail.getVolumeNetin()); // Assuming volumeNetin maps to mainForcesIn
                limitUpStockDailyDTO.setCirculatingMarketValue(stockDetail.getListedStock()); // Assuming listedStock maps to circulatingMarketValue
                limitUpStockDailyDTO.setPrice(stockDetail.getPrice());
                // Assuming other fields in TopicStockVO map directly or need specific handling
                // limitUpStockDailyDTO.setTurnoverRate(stockDetail.getTurnoverRate()); // Uncomment if present and needed
                // limitUpStockDailyDTO.setOpenTime(stockDetail.getOpenTime()); // Uncomment if present and needed
                // limitUpStockDailyDTO.setOpenTimes(stockDetail.getOpenTimes()); // Uncomment if present and needed
                // limitUpStockDailyDTO.setChangeRange(stockDetail.getChangeRange()); // Uncomment if present and needed
                // limitUpStockDailyDTO.setChangeRange5(stockDetail.getChangeRange5()); // Uncomment if present and needed
                // limitUpStockDailyDTO.setChangeRange5Max(stockDetail.getChangeRange5Max()); // Uncomment if present and needed
                // limitUpStockDailyDTO.setChangeRange1(stockDetail.getChangeRange1()); // Uncomment if present and needed
                // limitUpStockDailyDTO.setChangeRange1Max(stockDetail.getChangeRange1Max()); // Uncomment if present and needed
                // limitUpStockDailyDTO.setCallAuctionOrderTotal(stockDetail.getCallAuctionOrderTotal()); // Uncomment if present and needed
                limitUpStockDailyDTO.setLimitUpX(stockDetail.getLimitUpX());
                limitUpStockDailyDTO.setLimitUpN(stockDetail.getLimitUpN());
                limitUpStockDailyDTO.setLimitUpM(stockDetail.getLimitUpM());
                // limitUpStockDailyDTO.setTopicHotSum(stockDetail.getTopicHotSum()); // Uncomment if present and needed
                // limitUpStockDailyDTO.setLowRange(stockDetail.getLowRange()); // Uncomment if present and needed
                limitUpStockDailyDTO.setMainForces(stockDetail.getMainForces());
                limitUpStockDailyDTO.setCost(stockDetail.getCost());
                limitUpStockDailyDTO.setProfit(stockDetail.getProfit());
                // limitUpStockDailyDTO.setMainForcesIn(stockDetail.getMainForcesIn()); // Already mapped from volumeNetin, check if this is a different mainForcesIn
                limitUpStockDailyDTO.setDivergency(stockDetail.getDivergency());
                // limitUpStockDailyDTO.setMainForcesTime(stockDetail.getMainForcesTime()); // Uncomment if present and needed
                limitUpStockDailyDTO.setMainForcesCb(stockDetail.getMainForcesCB());
                limitUpStockDailyDTO.setMainForcesCs(stockDetail.getMainForcesCS());
                limitUpStockDailyDTO.setMainForcesBtimes(stockDetail.getMainForcesBtimes());
                limitUpStockDailyDTO.setMainForcesStimes(stockDetail.getMainForcesStimes());
                // limitUpStockDailyDTO.setDirection(stockDetail.getDirection()); // Uncomment if present and needed
                limitUpStockDailyDTO.setBuyAvgAmount(stockDetail.getBuyAvgAmount());
                limitUpStockDailyDTO.setSellAvgAmount(stockDetail.getSellAvgAmount());
                limitUpStockDailyList.add(limitUpStockDailyDTO);

                // 转换 StockTopicRelationInsertDTO
                // 一个股票可能关联多个主题，需要遍历 topics 数组 (TopicInfoVO[])
                if (stockDetail.getTopics() != null) {
                    for (TopicInfoVO topicInfo : stockDetail.getTopics()) {
                        StockTopicRelationInsertDTO stockTopicRelationDTO = new StockTopicRelationInsertDTO();
                        stockTopicRelationDTO.setTradeDate(tradeTime);
                        stockTopicRelationDTO.setWindCode(stockDetail.getWindCode());
                        stockTopicRelationDTO.setTopicId(topicInfo.getTopicId());
                        stockTopicRelationDTO.setTopic(topicInfo.getTopic());
                        stockTopicRelationDTO.setColor(topicInfo.getColor());
                        stockTopicRelationDTO.setTopicHot(topicInfo.getTopicHot()); // topicHot is double in TopicInfoVO
                        // Assuming TopicInfoVO does not have topicOrder, setting a default or based on logic
                        stockTopicRelationDTO.setTopicOrder(1); // Defaulting to 1, adjust if logic exists
                        stockTopicRelationDTO.setStatus(1); // 假设1为有效
                        stockTopicRelationList.add(stockTopicRelationDTO);
                    }
                }
            }
            // 先删除当天旧数据
            log.info("开始删除交易日 {} 的旧数据", tradeTime);
            limitUpMapper.deleteDailyStockSummaryByTradeDateAndFunctionId(tradeTime, "limit_up_stocks");
            limitUpMapper.deleteLimitUpStockDailyByTradeDate(tradeTime);
            limitUpMapper.deleteStockTopicRelationByTradeDate(tradeTime);
            log.info("删除交易日 {} 的旧数据完成", tradeTime);
            // 批量插入新数据
            log.info("开始批量插入新数据");
            if (!dailyStockSummaryList.isEmpty()) {
                limitUpMapper.batchInsertDailyStockSummary(dailyStockSummaryList);
                log.info("插入 DailyStockSummary {} 条", dailyStockSummaryList.size());
            }
            if (!limitUpStockDailyList.isEmpty()) {
                limitUpMapper.batchInsertLimitUpStockDaily(limitUpStockDailyList);
                log.info("插入 LimitUpStockDaily {} 条", limitUpStockDailyList.size());
            }
            if (!stockTopicRelationList.isEmpty()) {
                limitUpMapper.batchInsertStockTopicRelation(stockTopicRelationList);
                log.info("插入 StockTopicRelation {} 条", stockTopicRelationList.size());
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
