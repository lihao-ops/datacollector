package com.hao.datacollector.dto.param.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "题材映射股票列表查询参数")
public class TopicStockQueryParam {

    @Schema(description = "所属题材ID", example = "22")
    private Integer topicId;

    @Schema(description = "题材名称（模糊）", example = "BC电池")
    private String topicName;
}
