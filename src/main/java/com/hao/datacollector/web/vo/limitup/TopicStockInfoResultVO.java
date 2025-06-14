package com.hao.datacollector.web.vo.limitup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "主题下股票信息响应VO对象")
public class TopicStockInfoResultVO {

    @Schema(description = "主题ID", required = true)
    private int topicId;

    @Schema(description = "主题名称", required = true)
    private String topic;

    @Schema(description = "主题下股票信息列表", required = true)
    private List<TopicStockVO> stockList;
}