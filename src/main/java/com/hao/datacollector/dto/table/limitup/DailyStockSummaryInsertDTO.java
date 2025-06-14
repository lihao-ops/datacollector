package com.hao.datacollector.dto.table.limitup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "每日功能股票数量插入对象")
public class DailyStockSummaryInsertDTO {

    @Schema(description = "交易日", example = "2024-06-14")
    private String tradeDate;

    @Schema(description = "功能ID", example = "vol_boost")
    private String functionId;

    @Schema(description = "股票数量", example = "18")
    private Integer stockNum;

    @Schema(description = "数据状态：0无效,1有效", example = "1")
    private Integer status;
}
