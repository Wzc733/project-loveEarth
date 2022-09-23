package com.fwzc.rbcollect.core.pojo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName : UserLocalDTO
 * @Description : TODO
 * @Author : James
 * @Date : 2022/4/10 11:10
 * @Version 1.0
 */
@Data
@ApiModel(description="用户地址对象")
public class UserLocalDTO {
    @ApiModelProperty(value = "地址对象id")
    private String id;

    @ApiModelProperty(value = "完整地址的字符串")
    private String local;
}
