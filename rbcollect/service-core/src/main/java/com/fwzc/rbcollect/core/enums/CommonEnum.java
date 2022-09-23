package com.fwzc.rbcollect.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName : CommonEnum
 * @Description : TODO
 * @Author : James
 * @Date : 2022/4/10 13:47
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum CommonEnum {
    UN_FINISH(1, "未完成"),
    All_FINISH(2, "已完成"),
    IN_DELIVERY(3, "发货中"),
    IN_RECYCLING(4, "已联系人员上门回收"),
    IN_REVIEW(5, "审核中/处理中"),
    NO_SEE(6,"未查阅"),
    DONE_SEE(7,"已查阅"),

    APPLY_FOR_RECYCLING(101, "用户申请上门回收"),
    CANCEL_THE_RECYCLING(102, "用户取消上门回收"),
    BUY_GOODS(103, "用户申请商品购买"),
    CANCEL_BUY_GOODS(104, "用户取消商品购买"),
    CONFIRM_THE_DELIVARY_ADMIN(105, "管理员确认发货"),
    CALCEL_THE_DELIVARY_ADMIN(105, "管理员取消商品交易"),
    CONFIRM_THE_RECYCLING_ADMIN(106, "管理员确认垃圾上门回收"),
    CANCEL_THE_RECYCLING_ADMIN(109, "管理员取消垃圾上门回收"),


    LOCK_USER(111, "用户被锁定"),
    NORMAL_USER(112, "用户正常"),

    INTEGRAL_RECHARGE(201, "积分充值"),
    COMMODITY_TRADING(202, "商品交易"),
    RB_REBACK(203, "垃圾回收"),
    OTHER(204,"其它")
    ;

    private Integer Type;
    private String msg;


    public static String getMsgByType(int Type) {
        CommonEnum[] values = CommonEnum.values();
        for (CommonEnum obj : values) {
            if (Type == obj.getType().intValue()) {
                return obj.getMsg();
            }
        }
        return "";
    }
}
