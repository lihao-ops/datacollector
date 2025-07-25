package com.hao.datacollector.web.controller;

import com.hao.datacollector.dto.param.topic.TopicInfoParam;
import com.hao.datacollector.service.TopicService;
import com.hao.datacollector.web.vo.topic.TopicInfoKplVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Hao Li
 * @Date 2025-07-22 10:48:15
 * @description: 题材Controller
 */
@Slf4j
@Tag(name = "题材")
@RestController("topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Operation(summary = "转档题材库", method = "POST")
    @Parameters({
            @Parameter(name = "num", description = "提问内容对象", required = true)
    })
    @PostMapping("/kpl_job")
    public Boolean setKplTopicInfoJob(@RequestBody Integer num) {
        return topicService.setKplTopicInfoJob(num);
    }

    @GetMapping("topic_list")
    @Operation(summary = "获取题材信息列表", description = "支持多条件筛选查询题材信息")
    @Parameters({
            @Parameter(name = "topicId", description = "题材ID"),
            @Parameter(name = "name", description = "题材名称（模糊查询）"),
            @Parameter(name = "classLayer", description = "分类层级"),
            @Parameter(name = "plateSwitch", description = "板块开关"),
            @Parameter(name = "stkSwitch", description = "股票开关"),
            @Parameter(name = "isNew", description = "是否新增：1.是，0.否"),
            @Parameter(name = "minPower", description = "最小权重"),
            @Parameter(name = "maxPower", description = "最大权重"),
            @Parameter(name = "minSubscribe", description = "最小订阅数"),
            @Parameter(name = "minGoodNum", description = "最小点赞数"),
            @Parameter(name = "status", description = "状态：0.无效，1.有效"),
            @Parameter(name = "pageNum", description = "页码", example = "1"),
            @Parameter(name = "pageSize", description = "每页大小", example = "10")
    })
    public List<TopicInfoKplVO> getKplTopicInfoList(
            @RequestParam(required = false) Long topicId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String classLayer,
            @RequestParam(required = false) String plateSwitch,
            @RequestParam(required = false) String stkSwitch,
            @RequestParam(required = false) Integer isNew,
            @RequestParam(required = false) Integer minPower,
            @RequestParam(required = false) Integer maxPower,
            @RequestParam(required = false) Integer minSubscribe,
            @RequestParam(required = false) Integer minGoodNum,
            @RequestParam(required = false, defaultValue = "1") Integer status,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        TopicInfoParam queryDTO = TopicInfoParam.builder()
                .topicId(topicId)
                .name(name)
                .classLayer(classLayer)
                .plateSwitch(plateSwitch)
                .stkSwitch(stkSwitch)
                .isNew(isNew)
                .minPower(minPower)
                .maxPower(maxPower)
                .minSubscribe(minSubscribe)
                .minGoodNum(minGoodNum)
                .status(status)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .build();
        return topicService.getKplTopicInfoList(queryDTO);
    }

//    @GetMapping("category_stock_list")
//    @Operation(summary = "获取类别及关联股票列表", description = "查询题材类别信息及其关联的股票映射数据")
//    public List<TopicCategoryAndStockVO> getKplCategoryAndStockList(
//            @Parameter(description = "题材ID", example = "25") @RequestParam(required = false) Integer topicId,
//            @Parameter(description = "类别ID", example = "1534") @RequestParam(required = false) Integer categoryId,
//            @Parameter(description = "类别名称(模糊查询)", example = "材料") @RequestParam(required = false) String categoryName,
//            @Parameter(description = "股票代码", example = "300537.SZ") @RequestParam(required = false) String windCode,
//            @Parameter(description = "是否主做：1.是，0.否", example = "1") @RequestParam(required = false) String isZz,
//            @Parameter(description = "是否热门：1.是，0.否", example = "1") @RequestParam(required = false) String isHot,
//            @Parameter(description = "是否新增：1.是，0.否", example = "1") @RequestParam(required = false) Integer isNew,
//            @Parameter(description = "状态：0.无效,1.有效", example = "1") @RequestParam(required = false, defaultValue = "1") Integer status) {
//        return topicService.getKplCategoryAndStockList(topicId, categoryId, categoryName, windCode, isZz, isHot, isNew, status);
//    }
}