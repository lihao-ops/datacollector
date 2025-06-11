package com.hao.datacollector.web.vo.limitup;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TopicStockInfoResultVO {
    //@ApiModelProperty(value = "是否拥有权限", required = true)
    private Boolean permission;

    //@ApiModelProperty(value = "股票数量对象:Key表示对应的functionId,value表示对于的数量", required = true)
    private Map<String, String> stockNums;

    //@ApiModelProperty(value = "股票列表对象", required = true)
    private List<TopicStockVO> stockDetail;

    //@ApiModelProperty(value = "标签对象:key：topicID,value：TopicInfoVO对象", required = true)
    private Map<String, TopicInfoVO> topicList;
}