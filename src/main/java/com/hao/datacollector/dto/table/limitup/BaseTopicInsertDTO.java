package com.hao.datacollector.dto.table.limitup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Schema(name = "基础标签表对象")
public class BaseTopicInsertDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "标签ID")
    private Integer topicId;

    @Schema(description = "标签名称")
    private String topicName;

    @Schema(description = "数据状态:0.无效,1.有效(默认)")
    private Byte status;

    @Schema(description = "创建时间")
    private Timestamp createTime;

    @Schema(description = "更新时间")
    private Timestamp updateTime;
}