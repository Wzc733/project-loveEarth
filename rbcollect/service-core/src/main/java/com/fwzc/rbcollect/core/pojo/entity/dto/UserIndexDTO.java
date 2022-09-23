package com.fwzc.rbcollect.core.pojo.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Auther:wzc
 * @Data:2021/12/14 - 12 - 14 - 14:42
 */

@Data
@ApiModel(description = "首页用户信息")
public class UserIndexDTO {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "电子邮箱")
    private String email;


    @ApiModelProperty(value = "用户头像")
    private String headImg;


    @ApiModelProperty(value = "帐户可用积分")
    private int integral;

    @ApiModelProperty(value = "是否绑定地址")
    private boolean isBindLocal;


    @ApiModelProperty(value = "状态（0：锁定，1：正常 ）")
    private Integer status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "上次登录时间")
    private LocalDateTime lastLoginTime;

}