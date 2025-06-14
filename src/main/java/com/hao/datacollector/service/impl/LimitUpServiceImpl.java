package com.hao.datacollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.datacollector.common.cache.DateCache;
import com.hao.datacollector.common.constant.DataSourceConstant;
import com.hao.datacollector.common.constant.DateTimeFormatConstant;
import com.hao.datacollector.common.utils.DateUtil;
import com.hao.datacollector.common.utils.HttpUtil;
import com.hao.datacollector.dal.dao.LimitUpMapper;
import com.hao.datacollector.dto.table.limitup.DailyStockSummaryInsertDTO;
import com.hao.datacollector.service.LimitUpService;
import com.hao.datacollector.web.vo.limitup.LimitResultVO;
import com.hao.datacollector.web.vo.limitup.TopicStockInfoResultVO;
import com.hao.datacollector.web.vo.limitup.TopicStockVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
            log.info("开始执行涨停数据转档任务");
            // 获取涨停数据
            LimitResultVO<TopicStockInfoResultVO> limitData = getLimitUpData(tradeTime);
            if (limitData == null || !"200".equals(limitData.getResultCode()) || limitData.getResultObject() == null || limitData.getResultObject().getStockList().isEmpty()) {
                throw new RuntimeException("transferLimitUpDataToDatabase_end_but,resultData=null");
            }
            // 获取详情数据,进行分表转换
            TopicStockInfoResultVO resultData = limitData.getResultObject();
            List<TopicStockVO> stockDetails = resultData.getStockList();
            String topic = resultData.getTopic();
            int topicId = resultData.getTopicId();
            log.info("获取到 {} 条涨停股票数据，开始转档到数据库", stockDetails.size());
            DailyStockSummaryInsertDTO summaryInsertDTO = new DailyStockSummaryInsertDTO();
            summaryInsertDTO.setTradeDate(tradeTime);
            summaryInsertDTO.setFunctionId()

            // TODO: 这里需要添加数据库操作逻辑
            // 1. 创建对应的数据库表和实体类
            // 2. 使用JPA或MyBatis将数据保存到MySQL
            // 3. 可以考虑先清空当日数据，再插入新数据

            // 模拟数据库操作
            for (TopicStockVO stock : stockDetails) {
                log.debug("处理股票: {} - {}", stock.getWindCode(), stock.getName());
                // 这里应该调用DAO层保存数据
            }

//            String result = String.format("涨停数据转档成功，共处理 %d 条记录，总数量: %d",
//                    stockDetails.size(), resultData.getTotalNum());
//            log.info(result);

            return true;

        } catch (Exception e) {
            log.error("涨停数据转档失败", e);
            throw new RuntimeException("数据转档失败: " + e.getMessage(), e);
        }
    }
}
