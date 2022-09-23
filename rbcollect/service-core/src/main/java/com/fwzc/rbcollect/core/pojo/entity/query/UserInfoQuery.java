package com.fwzc.rbcollect.core.pojo.entity.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther:wzc
 * @Data:2021/12/1 - 12 - 01 - 16:48
 * 负责接收前端传进来的对象,同VO功能类型,但属于后台管理系统传进来的
 */
@Data
@ApiModel(description="用户搜索对象")
public class UserInfoQuery {

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "状态")
    private Integer status;

}