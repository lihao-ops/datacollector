package com.hao.datacollector.web.config;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hao.datacollector.common.utils.HttpUtil;
import com.hao.datacollector.dal.dao.NewsMapper;
import com.hao.datacollector.dto.param.news.NewsRequestParams;
import com.hao.datacollector.web.vo.news.NewsInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StrategyServiceTest {

    @Autowired
    private NewsMapper newsMapper;

    @Test
    public void testGetTopicStockInfo() {
        String url = "http://114.80.154.45/wstock_share/news/stock_news_list";
        HashMap<String, String> header = new HashMap<>(4);
        header.put("wind.sessionid", "9b2aee00b0244cb287825b0fb55105d6");
        NewsRequestParams params = new NewsRequestParams();
        params.setWindCode("600519.SH");
        // 发送请求，设置超时时间
        String bodyStr = HttpUtil.sendPostRequestTimeOut(url, JSON.toJSONString(params), 3000, header);
        JSONArray jsonArray = JSON.parseArray(bodyStr);
        Integer successCode = 200;
        if (jsonArray == null || !successCode.equals(jsonArray.get(0))) {
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
    }
}