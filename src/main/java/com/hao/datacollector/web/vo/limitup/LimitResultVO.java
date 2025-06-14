package com.hao.datacollector.web.vo.limitup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 涨停接口通用响应结果包装类
 * @param <T> 响应数据类型
 * @author hli
 * @since 2025-06-12
 */
@Data
@Schema(description = "涨停接口通用响应结果包装类")
public class LimitResultVO<T> {
    @Schema(description = "响应结果代码")
    private String resultCode;
    
    @Schema(description = "响应数据对象")
    private T resultObject;
    
    @Schema(description = "返回时间")
    private String returnTime;
}