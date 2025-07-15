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

    /**
     * 格式化数字精度（缩放并保留指定小数位数）
     * <p>
     * formatDecimal(14235, 1, true);   // 输出：1424.0 (14235/10=1423.5, 向上舍入到1424.0)
     * formatDecimal(14235, 1, false);  // 输出：1423.5 (14235/10=1423.5, 向下截断到1423.5)
     * formatDecimal(123456789, 5, true); // 输出：1234.56790 (缩放后向上舍入)
     * formatDecimal(123456789, 5, false); // 输出：1234.56789 (缩放后向下截断)
     *
     * @param originalValue 原始值
     * @param decimalPlaces 保留小数位数
     * @param roundUp       是否向上舍入，true为向上舍入，false为向下截断
     * @return 格式化后的数值
     */
    public static Double formatDecimal(Number originalValue, int decimalPlaces, boolean roundUp) {
        if (originalValue == null) return 0.0;
        BigDecimal value = new BigDecimal(originalValue.toString());
        // 获取整数部分的位数
        String integerPart = value.toBigInteger().toString();
        int integerDigits = integerPart.length();
        // 计算需要缩放的位数，使整数部分变为4位
        int targetIntegerDigits = 4;
        int scaleDown = Math.max(0, integerDigits - targetIntegerDigits);
        // 进行缩放
        if (scaleDown > 0) {
            BigDecimal divisor = BigDecimal.TEN.pow(scaleDown);
            // 保留足够的精度进行除法运算，这样才能正确处理舍入
            value = value.divide(divisor, decimalPlaces + 10, RoundingMode.HALF_UP);
        }
        // 根据roundUp参数选择舍入模式
        RoundingMode roundingMode;
        if (roundUp) {
            roundingMode = RoundingMode.UP;      // 远离零的方向舍入（向上）
        } else {
            roundingMode = RoundingMode.DOWN;    // 向零的方向舍入（向下截断）
        }
        // 设置最终的小数位数
        value = value.setScale(decimalPlaces, roundingMode);
        return value.doubleValue();
    }
}

