package com.hao.datacollector.dto.limitup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for stock_topic_relation table
 */
@Data
@Schema(description = "股票与概念关系DTO")
public class StockTopicRelationDTO {

    @Schema(description = "交易日期", example = "2023-10-01")
    private LocalDate tradeDate;

    @Schema(description = "Wind代码", example = "600519.SH")
    private String windCode;

    @Schema(description = "概念ID", example = "1001")
    private String topicId;

    @Schema(description = "概念名称", example = "白酒")
    private String topicName;

    @Schema(description = "概念排名", example = "1")
    private Integer topicRank;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}