package com.hao.datacollector.dal.dao;

import com.hao.datacollector.dto.table.quotation.QuotationStockBaseDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuotationMapper {
    /**
     * 批量插入基础行情数据
     *
     * @param quotationStockBaseList 行情数据列表
     * @return 插入数量
     */
    int ins0ertQuotationStockBaseList(@Param("baseQuotationList") List<QuotationStockBaseDTO> quotationStockBaseList);
}
