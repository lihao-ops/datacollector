package com.hao.datacollector.web.vo.topic;

import com.hao.datacollector.dto.table.topic.InsertStockCategoryMappingDTO;
import com.hao.datacollector.dto.table.topic.InsertTopicCategoryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Hao Li
 * @program: DataShareService
 * @Date 2025-07-24 16:59:25
 * @description: 题材版本包含所属股票代码VO对象
 */
@Data
public class TopicCategoryAndStockVO extends InsertTopicCategoryDTO {
    @Schema(description = "题材版本信息和所属股票信息List")
    private List<InsertStockCategoryMappingDTO> stockCategoryList;
}