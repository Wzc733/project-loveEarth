package com.fwzc.rbcollect.core.pojo.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther:wzc
 * @Data:2021/12/1 - 12 - 01 - 10:19
 * 用户的信息
 */
@Data
@ApiModel(description="用户信息对象")
public class UserInfoDTO {

    @ApiModelProperty(value = "用户姓名")
    private String name;


    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "JWT访问令牌")
    private String token;
}