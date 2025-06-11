package com.hao.datacollector.web.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * @author: mliu.jeremy
 * @createTime: 2021/8/30
 * @description: 本地缓存配置，每一个bean对应一种缓存策略
 */
@Configuration
public class CacheConfig {

    public static final int NORMAL_EXPIRE_TIME = 8;

    public static final int OCEAN_DATA_EXPIRE_TIME = 1;

    public static final int HOT_STOCK_INIT_NUM = 1000;

    public static final int HOT_STOCK_MAX_NUM = 2000;

    /**
     * 缓存管理器1
     *
     * @return
     */
    @Bean("caffeineCacheManager1")
    @Primary
    public CacheManager cacheManager1() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                // 写入缓存后8小时失效
                .expireAfterWrite(8, TimeUnit.HOURS)
                // 该大小指的是每个cacheName下面对应容器的初始大小
                .initialCapacity(10)
                // 该大小指的是每个cacheName下面对应容器的最大容量
                .maximumSize(200));
        return caffeineCacheManager;
    }
}