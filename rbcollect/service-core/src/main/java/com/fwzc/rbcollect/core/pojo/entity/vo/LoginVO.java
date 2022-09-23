package com.fwzc.rbcollect.core.pojo.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther:wzc
 * @Data:2021/12/1 - 12 - 01 - 10:17
 * 登录信息
 */
@Data
@ApiModel(description="登录对象")
public class LoginVO {


    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;
}