package com.hao.datacollector.web.controller;

import com.hao.datacollector.service.LimitUpService;
import com.hao.datacollector.web.vo.limitup.ApiResponse;
import com.hao.datacollector.web.vo.limitup.ResultObjectVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hli
 * @program: datacollector
 * @Date 2025-06-12 19:34:31
 * @description: 涨停相关接口
 */
@Slf4j
@RestController
@RequestMapping("limit_up")
@Tag(name = "涨停相关", description = "系统健康检查和状态监控接口")
public class LimitUpController {

    @Autowired
    private LimitUpService limitUpService;

    @Operation(summary = "涨停股票列表", description = "检查服务运行状态")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "服务正常运行"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "服务异常")
    })
    public ResponseEntity<ApiResponse<ResultObjectVO>> getLimitUpData(@RequestParam(required = false) String tradeTime) {
        ApiResponse<ResultObjectVO> result = limitUpService.getLimitUpData(tradeTime);
        if (result == null || !"200".equals(result.getResultCode())) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "将涨停数据转档到数据库", description = "检查服务运行状态")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "服务正常运行"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "服务异常")
    })
    @PostMapping("/transfer")
    public ResponseEntity<String> transferLimitUpData(@RequestParam(required = false) String tradeTime) {
        Boolean success = limitUpService.transferLimitUpDataToDatabase(tradeTime);
        if (!success) {
            return ResponseEntity.badRequest().body("数据转档失败");
        }
        return ResponseEntity.ok("数据转档成功");
    }
}
