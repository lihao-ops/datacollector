package com.hao.datacollector.web.vo.limitup;

import lombok.Data;

//@ApiModel("主题信息VO对象")
//extends BaseVO
@Data
public class TopicInfoVO {

    private static final long serialVersionUID = 1L;

    //@ApiModelProperty(value = "股票代码", required = true)
    private String windCode;

    //@ApiModelProperty(value = "标签ID", required = true)
    private int topicId;
    //@ApiModelProperty(value = "标签颜色", required = true)
    private String color;

    //@ApiModelProperty(value = "标签股票数量", required = true)
    private int stockNum;

    //@ApiModelProperty(value = "标签名称", required = true)
    private String topic;

    //@ApiModelProperty(value = "标签热度", required = true)
    private double topicHot;
}