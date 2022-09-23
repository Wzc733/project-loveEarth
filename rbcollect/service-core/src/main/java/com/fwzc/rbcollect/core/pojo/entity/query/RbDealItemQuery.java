package com.fwzc.rbcollect.core.pojo.entity.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName : RbDealItemQuery
 * @Description : TODO
 * @Author : James
 * @Date : 2022/4/3 14:05
 * @Version 1.0
 */
@Data
@ApiModel(description="垃圾回收订单搜索对象")
public class RbDealItemQuery {
    @ApiModelProperty(value = "申请垃圾回收的用户id")
    private Long rbUserId;
}
