package com.hao.datacollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.datacollector.common.cache.DateCache;
import com.hao.datacollector.common.constant.DataSourceConstant;
import com.hao.datacollector.common.constant.DateTimeFormatConstant;
import com.hao.datacollector.common.utils.DateUtil;
import com.hao.datacollector.common.utils.HttpUtil;
import com.hao.datacollector.dal.dao.LimitUpMapper;
import com.hao.datacollector.dto.table.limitup.DailyStockSummaryInsertDTO;
import com.hao.datacollector.dto.table.limitup.LimitUpStockDailyInsertDTO;
import com.hao.datacollector.dto.table.limitup.StockTopicRelationInsertDTO;
import com.hao.datacollector.service.LimitUpService;
import com.hao.datacollector.web.vo.limitup.LimitResultVO;
import com.hao.datacollector.web.vo.limitup.TopicStockInfoResultVO;
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

    @Value("wind_base.limit_up.url")
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
    public LimitResultVO<TopicStockInfoResultVO> getLimitUpData(String tradeTime) {
        try {
            if (!StringUtils.hasLength(tradeTime)) {
                tradeTime = DateUtil.getCurrentDateTime(DateTimeFormatConstant.EIGHT_DIGIT_DATE_FORMAT);
            }
            //非交易日无数据。
            if (!DateCache.ThisYearTradeDateList.contains(tradeTime)) {
                throw new RuntimeException("LimitUpServiceImpl_getLimitUpData_isNoTradeTime");
            }
            //url具体参数含义可查看TopicDetailParam,日期格式必须类似20250609
            String url = String.format(limitUpBaseUrl, tradeTime);
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set(DataSourceConstant.WIND_SESSION_NAME, windSessionId);
            String response = HttpUtil.sendGetRequest(DataSourceConstant.WIND_PROD_WGQ + url, headers, 10000, 30000).getBody();
            // 配置忽略未知字段，避免反序列化错误。解析JSON响应为LimitResultVO对象
            objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            LimitResultVO<TopicStockInfoResultVO> result = objectMapper.readValue(
                    response,
                    objectMapper.getTypeFactory().constructParametricType(
                            LimitResultVO.class,
                            TopicStockInfoResultVO.class
                    )
            );
            log.info("LimitUpServiceImpl_getLimitUpData,resultCode={}", result.getResultCode());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("LimitUpServiceImpl_getLimitUpData");
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
            LimitResultVO<TopicStockInfoResultVO> limitData = getLimitUpData(tradeTime);
            if (limitData == null || !"200".equals(limitData.getResultCode()) || limitData.getResultObject() == null || limitData.getResultObject().getStockList().isEmpty()) {
                log.warn("获取涨停数据为空或接口返回错误，交易日期: {}", tradeTime);
                return false;
            }
            // 获取详情数据,进行分表转换
            TopicStockInfoResultVO resultData = limitData.getResultObject();
            List<TopicStockVO> stockDetails = resultData.getStockList();
            String topic = resultData.getTopic();
            int topicId = resultData.getTopicId();
            log.info("获取到 {} 条涨停股票数据，主题: {} ({})，开始转档到数据库", stockDetails.size(), topic, topicId);
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
                limitUpStockDailyDTO.setVolumeNetin(stockDetail.getVolumeNetin());
                limitUpStockDailyDTO.setListedStock(stockDetail.getListedStock());
                limitUpStockDailyDTO.setPrice(stockDetail.getPrice());
                limitUpStockDailyDTO.setTurnoverRate(stockDetail.getTurnoverRate());
                limitUpStockDailyDTO.setOpenTime(stockDetail.getOpenTime());
                limitUpStockDailyDTO.setOpenTimes(stockDetail.getOpenTimes());
                limitUpStockDailyDTO.setChangeRange(stockDetail.getChangeRange());
                limitUpStockDailyDTO.setChangeRange5(stockDetail.getChangeRange5());
                limitUpStockDailyDTO.setChangeRange5Max(stockDetail.getChangeRange5Max());
                limitUpStockDailyDTO.setChangeRange1(stockDetail.getChangeRange1());
                limitUpStockDailyDTO.setChangeRange1Max(stockDetail.getChangeRange1Max());
                limitUpStockDailyDTO.setCallAuctionOrderTotal(stockDetail.getCallAuctionOrderTotal());
                limitUpStockDailyDTO.setLimitUpX(stockDetail.getLimitUpX());
                limitUpStockDailyDTO.setLimitUpN(stockDetail.getLimitUpN());
                limitUpStockDailyDTO.setLimitUpM(stockDetail.getLimitUpM());
                limitUpStockDailyDTO.setTopicHotSum(stockDetail.getTopicHotSum());
                limitUpStockDailyDTO.setLowRange(stockDetail.getLowRange());
                limitUpStockDailyDTO.setMainForces(stockDetail.getMainForces());
                limitUpStockDailyDTO.setCost(stockDetail.getCost());
                limitUpStockDailyDTO.setProfit(stockDetail.getProfit());
                limitUpStockDailyDTO.setMainForcesIn(stockDetail.getMainForcesIn());
                limitUpStockDailyDTO.setDivergency(stockDetail.getDivergency());
                limitUpStockDailyDTO.setMainForcesTime(stockDetail.getMainForcesTime());
                limitUpStockDailyDTO.setMainForcesCb(stockDetail.getMainForcesCB());
                limitUpStockDailyDTO.setMainForcesCs(stockDetail.getMainForcesCS());
                limitUpStockDailyDTO.setMainForcesBtimes(stockDetail.getMainForcesBtimes());
                limitUpStockDailyDTO.setMainForcesStimes(stockDetail.getMainForcesStimes());
                limitUpStockDailyDTO.setDirection(stockDetail.getDirection());
                limitUpStockDailyDTO.setBuyAvgAmount(stockDetail.getBuyAvgAmount());
                limitUpStockDailyDTO.setSellAvgAmount(stockDetail.getSellAvgAmount());
                limitUpStockDailyList.add(limitUpStockDailyDTO);

                // 转换 StockTopicRelationInsertDTO
                // 一个股票可能关联多个主题，需要遍历 topics 数组
                if (stockDetail.getTopics() != null) {
                    for (com.hao.datacollector.web.vo.limitup.TopicInfoVO topicInfo : stockDetail.getTopics()) {
                        StockTopicRelationInsertDTO stockTopicRelationDTO = new StockTopicRelationInsertDTO();
                        stockTopicRelationDTO.setTradeDate(tradeTime);
                        stockTopicRelationDTO.setWindCode(stockDetail.getWindCode());
                        stockTopicRelationDTO.setTopicId(topicInfo.getTopicId());
                        stockTopicRelationDTO.setTopic(topicInfo.getTopic());
                        stockTopicRelationDTO.setColor(topicInfo.getColor());
                        stockTopicRelationDTO.setTopicHot(Double.valueOf(topicInfo.getTopicHot()));
                        stockTopicRelationDTO.setTopicOrder(1); // 假设默认为主标签, 或根据业务逻辑设置
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
            // 注意：这里不应该再抛出RuntimeException，而是返回false，由调用方处理异常
            // throw new RuntimeException("数据转档失败: " + e.getMessage(), e);
            return false;
        }
    }
}
