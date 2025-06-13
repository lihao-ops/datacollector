package com.hao.datacollector.web.vo.limitup;

import lombok.Data;

/**
 * 涨停接口通用响应结果包装类
 * @param <T> 响应数据类型
 * @author hli
 * @since 2025-06-12
 */
@Data
public class LimitResultVO<T> {
    /**
     * 响应结果代码
     */
    private String resultCode;
    
    /**
     * 响应数据对象
     */
    private T resultObject;
    
    /**
     * 返回时间
     */
    private String returnTime;
}