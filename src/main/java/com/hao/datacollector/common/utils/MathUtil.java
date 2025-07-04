package com.hao.datacollector.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-07-04 23:29:08
 * @description: 数学计算相关Util
 */
public class MathUtil {
    /**
     * 截取小数点后N位
     *
     * @param valueStr 待截取的数字字符串
     * @param digits   n位
     * @return 移动后N位数字
     */
    public static BigDecimal shiftDecimal(String valueStr, int digits) {
        BigDecimal raw = new BigDecimal(valueStr);

        if (digits >= 0) {
            BigDecimal divisor = BigDecimal.TEN.pow(digits);
            return raw.divide(divisor, 2, RoundingMode.HALF_UP);
        } else {
            BigDecimal multiplier = BigDecimal.TEN.pow(-digits);
            return raw.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
        }
    }
}

