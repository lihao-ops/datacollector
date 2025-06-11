package com.hao.datacollector.web.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j API 文档配置
 * 量化交易数据收集器服务 API 文档
 * http://localhost:8001/data-collector/doc.htm
 */
@Configuration
public class OpenApiConfig {

    /**
     * 默认 API 分组，包含前端和后端接口
     */
    @Bean
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
                .group("default")  // 分组名称
                .packagesToScan("com.hao.datacollector.web.controller") // 匹配路径，可以根据实际情况调整
                .build();
    }

//    /**
//     * 前端 API 分组，只包含前端接口
//     */
//    @Bean
//    public GroupedOpenApi dataCollectionApi() {
//        return GroupedOpenApi.builder()
//                .group("数据采集接口")  // 分组名称
//                .pathsToMatch("/data_collection/**")  // 前端接口路径
//                .build();
//    }
//
//    /**
//     * 后端 API 分组，只包含后端接口
//     */
//    @Bean
//    public GroupedOpenApi adminApi() {
//        return GroupedOpenApi.builder()
//                .group("后端接口")  // 分组名称
//                .pathsToMatch("/api/admin/**")  // 后端接口路径
//                .build();
//    }

    /**
     * 定制全局信息
     */
//    @Bean
//    public OpenApiCustomizer customOpenApiCustomizer() {
//        return openApi -> openApi.info(
//                new Info()
//                        .title("数据服务接口文档")
//                        .description("文档描述")
//                        .version("1.0.0")
//                        .contact(new Contact().name("hli").email("api-team@example.com"))
//        );
//    }

    /**
     * 定制全局信息
     */
    @Bean
    public OpenApiCustomizer customOpenApiCustomizer() {
        return openApi -> {
            openApi.info(
                    new io.swagger.v3.oas.models.info.Info()
                            .title("量化交易数据收集器 API")
                            .description("量化交易数据服务模块：实现数据采集(包括定时拉取第三方数据)、清洗、存储等任务的 RESTful API 接口文档")
                            .version("1.0.0")
                            .contact(new io.swagger.v3.oas.models.info.Contact()
                                    .name("量化交易开发团队")
                                    .email("quant-dev@example.com")
                                    .url("https://github.com/your-org/datacollector"))
                            .license(new io.swagger.v3.oas.models.info.License()
                                    .name("Apache 2.0")
                                    .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
            );

            // 添加标签
            openApi.addTagsItem(new io.swagger.v3.oas.models.tags.Tag()
                    .name("数据采集")
                    .description("股票、期货、基金等金融数据采集接口"));
            openApi.addTagsItem(new io.swagger.v3.oas.models.tags.Tag()
                    .name("系统管理")
                    .description("系统监控、健康检查和配置管理接口"));
        };
    }
}