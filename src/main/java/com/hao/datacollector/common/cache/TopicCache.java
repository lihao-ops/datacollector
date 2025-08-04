package com.hao.datacollector.common.cache;

import com.alibaba.fastjson.JSON;
import com.hao.datacollector.dal.dao.TopicMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-08-03 22:09:00
 * @description: 题材相关缓存
 */
@Slf4j
@Component("TopicCache")
public class TopicCache {
    @Autowired
    private TopicMapper topicMapper;
    /**
     * 题材信息缓存Map
     * key:题材id
     * value:题材所属类别下股票代码列表
     * <p>
     * 思路:
     * 1.先查询tb_topic_info表中所有的题材id List
     * 2.遍历查询所有
     */
    public static Map<String, List<String>> topicMap = new HashMap<>();

    @PostConstruct
    public void initTopicCache() {
        //获取所有题材idList
        List<Integer> allTopicIdList = topicMapper.getKplAllTopicIdList();
        log.info("TopicCache_allTopicIdList.size={}", allTopicIdList.size());
    }
}
