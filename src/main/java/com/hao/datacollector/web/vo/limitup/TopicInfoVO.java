package com.hao.datacollector.web.vo.limitup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "主题信息VO对象")
public class TopicInfoVO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "股票代码", required = true)
    private String windCode;

    @Schema(description = "标签ID", required = true)
    private int topicId;

    @Schema(description = "标签颜色", required = true)
    private String color;

    @Schema(description = "标签股票数量", required = true)
    private int stockNum;

    @Schema(description = "标签名称", required = true)
    private String topic;

    @Schema(description = "标签热度", required = true)
    private double topicHot;
}