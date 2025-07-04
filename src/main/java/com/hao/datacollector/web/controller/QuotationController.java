package com.hao.datacollector.web.controller;

import com.hao.datacollector.service.QuotationService;
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
 * @Date 2025-07-04 17:42:14
 * @description: 行情相关controller
 */
@Tag(name = "行情模块", description = "新闻模块数据处理接口")
@Slf4j
@RestController
@RequestMapping("/quotation")
public class QuotationController {
    @Autowired
    private QuotationService quotationService;

    @Operation(summary = "转档股票新闻数据", description = "检查服务运行状态")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "服务正常运行"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "服务异常")
    })
    @PostMapping("/transfer")
    public ResponseEntity<String> transferQuotationByStock(@RequestParam(required = false) String windCode) {
        Boolean success = quotationService.transferQuotationByStock(windCode);
        if (!success) {
            return ResponseEntity.badRequest().body("数据转档失败");
        }
        return ResponseEntity.ok("数据转档成功");
    }
}
