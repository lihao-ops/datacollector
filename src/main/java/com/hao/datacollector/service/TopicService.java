package com.hao.datacollector.service;


import com.hao.datacollector.dto.param.topic.TopicInfoParam;
import com.hao.datacollector.web.vo.topic.TopicInfoKplVO;

import java.util.List;

/**
 * @author Hao Li
 * @Date 2025-07-22 10:49:42
 * @description: 题材service
 */
public interface TopicService {
    /**
     * 转档题材库
     *
     * @param num 遍历题材id的数量
     * @return 转档结果
     */
    Boolean setKplTopicInfoJob(Integer num);

    /**
     * 获取热门题材信息列表
     *
     * @param queryDTO 题材信息查询参数对象，包含分页、筛选、排序等条件
     * @return 题材信息列表
     */
    List<TopicInfoKplVO> getKplTopicInfoList(TopicInfoParam queryDTO);
}