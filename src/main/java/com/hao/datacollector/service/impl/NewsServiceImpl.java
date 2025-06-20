package com.hao.datacollector.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hao.datacollector.common.constant.CommonConstant;
import com.hao.datacollector.common.constant.DataSourceConstant;
import com.hao.datacollector.common.utils.HttpUtil;
import com.hao.datacollector.dal.dao.NewsMapper;
import com.hao.datacollector.dto.param.news.NewsRequestParams;
import com.hao.datacollector.service.NewsService;
import com.hao.datacollector.web.vo.news.NewsInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-06-20 17:10:56
 * @description: 新闻相关实现类
 */
@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Value("${wind_base.session_id}")
    private String windSessionId;

    @Value("wind_base.news.stock_news_url")
    private String stockNewsUrl;

    /**
     * 转档股票新闻数据
     *
     * @param windCode 股票代码
     * @return 操作结果
     */
    @Override
    public Boolean transferNewsStockData(String windCode) {
        String url = DataSourceConstant.WIND_PROD_WGQ + stockNewsUrl;
        HashMap<String, String> header = new HashMap<>(4);
        header.put(DataSourceConstant.WIND_POINT_SESSION_NAME, windSessionId);
        NewsRequestParams params = new NewsRequestParams();
        params.setWindCode(windCode);
        // 发送请求，设置超时时间
        String bodyStr = HttpUtil.sendPostRequestTimeOut(url, JSON.toJSONString(params), 3000, header);
        JSONArray jsonArray = JSON.parseArray(bodyStr);
        if (jsonArray == null || !CommonConstant.successCode.equals(jsonArray.get(0))) {
            log.error("transferNewsStockData_error,windCode={}", windCode);
            throw new RuntimeException("数据异常");
        }
        JSONArray newsArray = JSON.parseArray(jsonArray.getJSONObject(3).getString("value")).getJSONObject(0).getJSONArray("news");
        List<NewsInfoVO> newInfoVOList = JSON.parseArray(newsArray.toJSONString(), NewsInfoVO.class);
        int newsInfoResultCount = newsMapper.insertNewsInfo(newInfoVOList);
        List<String> newsIdList = newInfoVOList.stream()
                .map(NewsInfoVO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        int relationResultCount = newsMapper.insertNewsStockRelation(newsIdList, params.getWindCode());
        log.info("newsInfoResultCount={},relationResultCount={}", newsInfoResultCount, relationResultCount);
        return newsInfoResultCount >= 0;
    }
}
