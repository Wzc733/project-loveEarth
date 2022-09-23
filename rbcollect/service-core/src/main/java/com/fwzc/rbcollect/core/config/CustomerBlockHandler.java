package com.fwzc.rbcollect.core.config;

/**
 * @ClassName : CustomerBlockHandler
 * @Description : TODO
 * @Author : James
 * @Date : 2022/5/12 12:46
 * @Version 1.0
 */

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * 此类型用来处理限流自定义逻辑
 * @author 王梓超
 */
public class CustomerBlockHandler {
    public static String handlerException1(BlockException exception){
        return "handlerException1：系统异常，请稍后重试！";
    }
    public static String handlerException2(BlockException exception){
        return "handlerException2：网络崩溃了，请稍后重试！";
    }
}