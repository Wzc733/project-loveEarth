package com.fwzc.rbcollect.core.pojo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName : UserInfoDTO
 * @Description : TODO
 * @Author : James
 * @Date : 2022/4/3 16:59
 * @Version 1.0
 */
@Data
@ApiModel(description="日期用户数量对象")
public class RbDealChartsDTO {

    @ApiModelProperty(value = "创建时间")
    private String createTime;


    @ApiModelProperty(value = "垃圾回收完成次数")
    private String count;
}
