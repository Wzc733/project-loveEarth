package com.fwzc.rbcollect.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 垃圾回收下单订单
 * </p>
 *
 * @author wzc
 * @since 2022-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RbDealItem对象", description="垃圾回收下单订单")
public class RbDealItem implements Serializable {

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

    @ApiModelProperty(value = "申请垃圾回收用户id")
    private Long rbUserId;

    @ApiModelProperty(value = "申请垃圾回收用户名称")
    private String rbUserName;

    @ApiModelProperty(value = "垃圾名称")
    private String rbName;

    @ApiModelProperty(value = "垃圾类型")
    private String rbType;

    @ApiModelProperty(value = "垃圾重量(單位：斤)")
    private String rbWeight;

    @ApiModelProperty(value = "上门地址")
    private String deliveryUserLocal;

    @ApiModelProperty(value = "用户姓名")
    private String deliveryUserName;

    @ApiModelProperty(value = "用户电话")
    private String deliveryUserMobile;


    @ApiModelProperty(value = "预期收益积分")
    private Integer expectIntegral;

    @ApiModelProperty(value = "状态（0：默认 1：未完成 2：已完成）")
    private String status;

    @ApiModelProperty(value = "用户是否取消申请垃圾回收")
    private boolean isCancleCycle;


    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "垃圾回收上门时间")
    private LocalDateTime serviceTime;

    @ApiModelProperty(value = "逻辑删除(1:已删除，0:未删除)")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;

   

}
