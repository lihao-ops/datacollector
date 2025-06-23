package com.hao.datacollector.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExtremeValueUtil {

    // 默认极值阈值配置
    private static final Map<Class<?>, Number> DEFAULT_MAX_VALUES = new HashMap<>();

    static {
        DEFAULT_MAX_VALUES.put(Double.class, 999999999.99);
        DEFAULT_MAX_VALUES.put(double.class, 999999999.99);
        DEFAULT_MAX_VALUES.put(Float.class, 999999999.99f);
        DEFAULT_MAX_VALUES.put(float.class, 999999999.99f);
        DEFAULT_MAX_VALUES.put(Integer.class, 999999999);
        DEFAULT_MAX_VALUES.put(int.class, 999999999);
        DEFAULT_MAX_VALUES.put(Long.class, 999999999999L);
        DEFAULT_MAX_VALUES.put(long.class, 999999999999L);
    }

    /**
     * 处理列表中所有对象的极值字段
     *
     * @param list 待处理的对象列表
     * @param <T>  泛型类型
     */
    public static <T> void handleExtremeValues(List<T> list) {
        handleExtremeValues(list, null);
    }

    /**
     * 处理列表中所有对象的极值字段（自定义阈值）
     *
     * @param list            待处理的对象列表
     * @param customMaxValues 自定义字段最大值配置 key:字段名 value:最大值
     * @param <T>             泛型类型
     */
    public static <T> void handleExtremeValues(List<T> list, Map<String, Number> customMaxValues) {
        if (list == null || list.isEmpty()) {
            return;
        }

        for (T obj : list) {
            handleSingleObject(obj, customMaxValues);
        }
    }

    /**
     * 处理单个对象的极值字段
     */
    private static <T> void handleSingleObject(T obj, Map<String, Number> customMaxValues) {
        if (obj == null) {
            return;
        }

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);

                if (value == null) {
                    continue;
                }

                Class<?> fieldType = field.getType();
                String fieldName = field.getName();

                // 检查是否为数值类型
                if (isNumericType(fieldType)) {
                    Number numValue = (Number) value;
                    Number maxValue = getMaxValue(fieldName, fieldType, customMaxValues);

                    if (isExtremeValue(numValue, maxValue, fieldType)) {
                        setDefaultValue(obj, field, fieldType);
                        log.warn("字段 {} 存在极值 {}，已设置为默认值", fieldName, numValue);
                    }
                }

            } catch (Exception e) {
                log.error("处理字段 {} 时发生异常", field.getName(), e);
            }
        }
    }

    /**
     * 判断是否为数值类型
     */
    private static boolean isNumericType(Class<?> type) {
        return type == double.class || type == Double.class ||
                type == float.class || type == Float.class ||
                type == int.class || type == Integer.class ||
                type == long.class || type == Long.class;
    }

    /**
     * 获取字段的最大值阈值
     */
    private static Number getMaxValue(String fieldName, Class<?> fieldType, Map<String, Number> customMaxValues) {
        // 优先使用自定义配置
        if (customMaxValues != null && customMaxValues.containsKey(fieldName)) {
            return customMaxValues.get(fieldName);
        }

        // 使用默认配置
        return DEFAULT_MAX_VALUES.get(fieldType);
    }

    /**
     * 判断是否为极值
     */
    private static boolean isExtremeValue(Number value, Number maxValue, Class<?> fieldType) {
        if (maxValue == null) {
            return false;
        }

        // 检查无穷大和NaN
        if (fieldType == double.class || fieldType == Double.class) {
            double doubleValue = value.doubleValue();
            if (!Double.isFinite(doubleValue)) {
                return true;
            }
            return doubleValue > maxValue.doubleValue();
        }

        if (fieldType == float.class || fieldType == Float.class) {
            float floatValue = value.floatValue();
            if (!Float.isFinite(floatValue)) {
                return true;
            }
            return floatValue > maxValue.floatValue();
        }

        // 整数类型比较
        return value.longValue() > maxValue.longValue();
    }

    /**
     * 设置默认值
     */
    private static void setDefaultValue(Object obj, Field field, Class<?> fieldType) throws IllegalAccessException {
        if (fieldType == double.class || fieldType == Double.class) {
            field.set(obj, 0.0);
        } else if (fieldType == float.class || fieldType == Float.class) {
            field.set(obj, 0.0f);
        } else if (fieldType == int.class || fieldType == Integer.class) {
            field.set(obj, 0);
        } else if (fieldType == long.class || fieldType == Long.class) {
            field.set(obj, 0L);
        }
    }
}