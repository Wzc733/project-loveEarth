package com.fwzc.rbcollect.core.pojo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品评价表
 * </p>
 *
 * @author wzc
 * @since 2022-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GoodsEvaluate对象", description="商品评价表")
public class GoodsEvaluateDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "商品评价")
    private String evaluateContent;

    @ApiModelProperty(value = "商品评分")
    private String commodityGrade;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户头像")
    private String headImg;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
