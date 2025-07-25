package com.hao.datacollector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.datacollector.common.utils.PageRuleUtil;
import com.hao.datacollector.dal.dao.TopicMapper;
import com.hao.datacollector.dto.PageNumDTO;
import com.hao.datacollector.dto.kpl.CategoryLevel;
import com.hao.datacollector.dto.kpl.HotTopicKpl;
import com.hao.datacollector.dto.kpl.StockDetail;
import com.hao.datacollector.dto.kpl.TopicTable;
import com.hao.datacollector.dto.param.topic.TopicInfoParam;
import com.hao.datacollector.dto.table.topic.InsertStockCategoryMappingDTO;
import com.hao.datacollector.dto.table.topic.InsertTopicCategoryDTO;
import com.hao.datacollector.dto.table.topic.InsertTopicInfoDTO;
import com.hao.datacollector.service.TopicService;
import com.hao.datacollector.web.vo.topic.TopicInfoKplVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hao Li
 * @Date 2025-07-22 10:50:08
 * @description: 题材实现类
 */
@Slf4j
@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper topicMapper;


    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 转档题材库
     *
     * @param num 遍历题材id的数量
     * @return 转档结果
     */
    @Override
    public Boolean setKplTopicInfoJob(Integer num) {
        try {
            // 读取本地文件内容
            String filePath = "C:\\Users\\hli.lihao\\Desktop\\response.txt";
            String content = Files.readString(Paths.get(filePath));
            log.info("setKplTopicInfoJob_response.size={}", content.length());
            // 解析JSON为对象
            HotTopicKpl hotTopic = objectMapper.readValue(content, HotTopicKpl.class);
            //转换插入
            return insertKplTopicInsertData(hotTopic);

        } catch (IOException e) {
            System.err.println("读取文件失败：" + e.getMessage());
            return false; // 处理失败返回false
        }
    }

    /**
     * 插入题材相关数据
     *
     * @param hotTopic 题材对象
     * @return 插入结果
     */
    private Boolean insertKplTopicInsertData(HotTopicKpl hotTopic) {
        log.info("insertKplTopicInsertData_start_processing_topic_data,topicId={}", hotTopic.getId());
        //先转换
        InsertTopicInfoDTO insertTopicInfoDTO = new InsertTopicInfoDTO();
        BeanUtils.copyProperties(hotTopic, insertTopicInfoDTO);
        insertTopicInfoDTO.setTopicId(Integer.valueOf(hotTopic.getId()));
        //主题时间
        insertTopicInfoDTO.setTopicCreateTime(hotTopic.getCreateTime());
        insertTopicInfoDTO.setTopicUpdateTime(hotTopic.getUpdateTime());
        //类别信息列表
        List<InsertTopicCategoryDTO> insertCategoryList = new ArrayList<>();
        //类别所属股票映射信息列表
        List<InsertStockCategoryMappingDTO> insertStockCategoryMappingList = new ArrayList<>();
        List<TopicTable> categoryList = hotTopic.getTable();
        if (categoryList == null || categoryList.isEmpty()) {
            log.warn("insertKplTopicInsertData_category_list_is_empty,_topicId={}", hotTopic.getId());
            return false;
        }
        log.info("insertKplTopicInsertData_start_processing_categories,topicId={},category_count={}", hotTopic.getId(), categoryList.size());
        for (TopicTable category : categoryList) {
            InsertTopicCategoryDTO insertCategoryLevel1 = new InsertTopicCategoryDTO();
            CategoryLevel level1 = category.getLevel1();
            BeanUtils.copyProperties(level1, insertCategoryLevel1);
            insertCategoryLevel1.setTopicId(Integer.valueOf(hotTopic.getId()));
            //一级类别数据parentId = 0
            insertCategoryLevel1.setParentId(0);
            insertCategoryLevel1.setCategoryId(Integer.valueOf(level1.getId()));
            insertCategoryList.add(insertCategoryLevel1);
            //一级类别股票映射信息
            List<StockDetail> level1Stocks = level1.getStocks();
            if (level1Stocks != null) {
                for (StockDetail level1Stock : level1Stocks) {
                    InsertStockCategoryMappingDTO stockMappingDTO = new InsertStockCategoryMappingDTO();
                    BeanUtils.copyProperties(level1Stock, stockMappingDTO);
                    stockMappingDTO.setWindCode(getWindCodeMapping(level1Stock.getStockId()));
                    stockMappingDTO.setCategoryId(insertCategoryLevel1.getCategoryId());
                    insertStockCategoryMappingList.add(stockMappingDTO);
                }
            }
            List<CategoryLevel> level2List = category.getLevel2();
            if (level2List != null) {
                for (CategoryLevel level2 : level2List) {
                    InsertTopicCategoryDTO insertCategoryLevel2 = new InsertTopicCategoryDTO();
                    BeanUtils.copyProperties(level2, insertCategoryLevel2);
                    insertCategoryLevel2.setTopicId(Integer.valueOf(hotTopic.getId()));
                    insertCategoryLevel2.setCategoryId(Integer.valueOf(level2.getId()));
                    //指定一级类别id为父id
                    insertCategoryLevel2.setParentId(insertCategoryLevel1.getCategoryId());
                    insertCategoryList.add(insertCategoryLevel2);

                    //二级类别股票映射信息
                    List<StockDetail> level2Stocks = level2.getStocks();
                    for (StockDetail level2Stock : level2Stocks) {
                        InsertStockCategoryMappingDTO stockMappingDTO = new InsertStockCategoryMappingDTO();
                        BeanUtils.copyProperties(level2Stock, stockMappingDTO);
                        stockMappingDTO.setWindCode(getWindCodeMapping(level2Stock.getStockId()));
                        stockMappingDTO.setCategoryId(insertCategoryLevel2.getCategoryId());
                        insertStockCategoryMappingList.add(stockMappingDTO);
                    }
                }
            }
        }
        log.info("insertKplTopicInsertData_data_processing_completed,topicId={},_total_categories={},total_stock_mappings={}", hotTopic.getId(), insertCategoryList.size(), insertStockCategoryMappingList.size());
        List<InsertTopicInfoDTO> insertTopicInfoList = new ArrayList();
        insertTopicInfoList.add(insertTopicInfoDTO);
        return insertTopicInfo(insertTopicInfoList, insertCategoryList, insertStockCategoryMappingList);
    }

    /**
     * 插入转档题材相关数据kpl
     *
     * @param insertTopicInfoList            题材信息list
     * @param insertCategoryList             类别信息list
     * @param insertStockCategoryMappingList 股票映射信息list
     * @return 操作结果
     */
    private Boolean insertTopicInfo(List<InsertTopicInfoDTO> insertTopicInfoList, List<InsertTopicCategoryDTO> insertCategoryList, List<InsertStockCategoryMappingDTO> insertStockCategoryMappingList) {
        int insertTopicNum = topicMapper.insertTopicInfoList(insertTopicInfoList);
        int insertCategoryNum = topicMapper.insertCategoryList(insertCategoryList);
        int insertStockNum = topicMapper.insertStockCategoryMappingList(insertStockCategoryMappingList);
        log.info("insertTopicInfo_insertTopicNum={},insertCategoryNum={},insertStockNum={}", insertTopicNum, insertCategoryNum, insertStockNum);
        return insertTopicNum + insertCategoryNum + insertStockNum > 0;
    }

    /**
     * 获取映射带有后缀的股票代码
     *
     * @param stockId 未带后缀股票代码
     * @return 带有后缀股票代码
     */
    private String getWindCodeMapping(String stockId) {
        return stockId + ".SH";
    }

    /**
     * 获取热门题材信息列表
     *
     * @param queryDTO 题材信息查询参数对象，包含分页、筛选、排序等条件
     * @return 题材信息列表
     */
    @Override
    public List<TopicInfoKplVO> getKplTopicInfoList(TopicInfoParam queryDTO) {
        // 获取分页参数
        PageNumDTO pageParam = PageRuleUtil.getPageParam(queryDTO.getPageNo(), queryDTO.getPageSize(), 1);
        queryDTO.setPageNo(pageParam.getPageNo());
        queryDTO.setPageSize(pageParam.getPageSize());
        return topicMapper.getKplTopicInfoList(queryDTO);
    }
}