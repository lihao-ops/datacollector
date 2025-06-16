//package com.hao.datacollector.web.controller;
//
//import com.hao.datacollector.service.LimitUpService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author hli
// * @program: datacollector
// * @Date 2025-06-12 19:31:55
// * @description: 涨停相关
// */
//@Slf4j
//@Tag(name = "涨停相关", description = "涨停相关数据采集与获取接口")
//@RestController
//@RequestMapping("limit_up")
//@Schema(description = "涨停相关控制器")
//public class LimitUpController {
//
//    @Autowired
//    private LimitUpService limitUpService;
//
//    @Operation(summary = "涨停转档", description = "获取涨停股票信息数据")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "获取成功"),
//            @ApiResponse(responseCode = "500", description = "服务异常")
//    })
//    @PostMapping("/data")
//    @Schema(description = "获取涨停转档数据并转档")
//    public Boolean setLimitUpData(String tradeTime) {
//        log.info("接收到涨停转档请求");
//        Boolean result = limitUpService.transferLimitUpDataToDatabase(tradeTime);
//        log.info("涨停转档请求处理完成={}", result);
//        return result;
//    }
//}
