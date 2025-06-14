package com.hao.datacollector.dto.table.limitup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "股票标签关联插入对象")
public class StockTopicRelationInsertDTO {

    @Schema(description = "交易日", example = "2024-06-14")
    private String tradeDate;

    @Schema(description = "股票代码", example = "600519.SH")
    private String windCode;

    @Schema(description = "标签ID", example = "1001")
    private Integer topicId;

    @Schema(description = "标签颜色(十六进制)", example = "#FF5733")
    private String color;

    @Schema(description = "标签名称", example = "机器人概念")
    private String topic;

    @Schema(description = "标签热度")
    private Double topicHot;

    @Schema(description = "标签排序(1主标签,2次标签)", example = "1")
    private Integer topicOrder;

    @Schema(description = "数据状态：0无效,1有效", example = "1")
    private Integer status;
}
