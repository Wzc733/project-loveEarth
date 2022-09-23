package com.fwzc.rbcollect.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品购买下单订单
 * </p>
 *
 * @author wzc
 * @since 2022-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GoodsDealItem对象", description="商品购买下单订单")
public class GoodsDealItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "交易流水id")
    private Long transFlowId;

    @ApiModelProperty(value = "用户地址id")
    private Long userLocalId;

    @ApiModelProperty(value = "交易编号")
    private String dealItemNo;

    @ApiModelProperty(value = "购买商品用户id")
    private Long payUserId;

    @ApiModelProperty(value = "购买商品用户名称")
    private String payUserName;

    @ApiModelProperty(value = "商品id")
    private String goodsId;

    @ApiModelProperty(value = "商品评价次数是否为2")
    private boolean isEvaluateFull;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "上门地址")
    private String deliveryUserLocal;

    @ApiModelProperty(value = "用户姓名")
    private String deliveryUserName;

    @ApiModelProperty(value = "用户电话")
    private String deliveryUserMobile;

    @ApiModelProperty(value = "用户是否取消申请垃圾回收")
    private boolean isCancleCycle;


    @ApiModelProperty(value = "状态（0：默认 1：未完成 2：已完成）")
    private String status;

    @ApiModelProperty(value = "购买数量")
    private Integer nums;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除(1:已删除，0:未删除)")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;




}
