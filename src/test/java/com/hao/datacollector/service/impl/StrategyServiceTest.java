package com.hao.datacollector.service.impl;


import com.hao.datacollector.web.vo.limitup.ApiResponse;
import com.hao.datacollector.web.vo.limitup.ResultObjectVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StrategyServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetTopicStockInfo() {
        String url = "http://114.80.154.45/strategyservice/limitupbogy/topicstockinfo?dateTime=20250609&stockFilterIds=4&functionId=1&topicIds=-1&sortFieldId=2&sortType=1&pageFlag=true&currentPage=1&pageSize=1000";

        HttpHeaders headers = new HttpHeaders();
        headers.set("windsessionid", "03600f719def444dbbcb51446c6b2824");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ApiResponse<ResultObjectVO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<ApiResponse<ResultObjectVO>>() {
                }
        );

        assertNotNull(response.getBody());
        assertEquals("200", response.getBody().getResultCode());
        assertNotNull(response.getBody().getResultObject());
        assertNotNull(response.getBody().getResultObject().getStockDetail());
        assertNotNull(response.getBody().getResultObject().getStockNums());
        assertNotNull(response.getBody().getResultObject().getTopicList());
        // You can add more assertions here to validate the structure and content of resultObject
        // For example:
        // assertTrue(response.getBody().getResultObject().getStockDetail().size() > 0);
    }
}