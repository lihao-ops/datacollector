//package com.hao.datacollector.web.vo.result;
//
//import lombok.Data;
//
//@Data
//public class ResultVO<T> {
//    //@ApiModelProperty(value = "状态码 200：OK,401：Unauthorized,403: Forbidden,404：Not Found", required = true)
//    private Integer code = ResultEnum.SUCCESSFUL.getCode();
//
//    //@ApiModelProperty(value = "返回请求信息", required = true)
//    protected String message = ResultEnum.SUCCESSFUL.getMessage();
//
//    //@ApiModelProperty(value = "返回结果对象", required = true)
//    private T data;
//}