package com.fwzc.common.result;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Auther:wzc
 * @Data:2021/11/23 - 11 - 23 - 20:22
 */
@Data
public class Result {
    private Integer code;
    private String message;
    private Map<String,Object> data=new HashMap<>();
    private Set set=new HashSet();
    private String[] strings;

    private Result(){}
    private String content;

    /**
     * 成功
     * @return
     */
    public static Result ok(){
        Result result = new Result();
        result.setCode(ResponseEnum.SUCCESS.getCode());
        result.setMessage(ResponseEnum.SUCCESS.getMessage());
        return result;
    }
    /**
     * 失败
     */
    public static Result error(){
        Result result = new Result();
        result.setCode(ResponseEnum.ERROR.getCode());
        result.setMessage(ResponseEnum.ERROR.getMessage());
        return result;
    }

    /**
     * 设置特定的结果
     * @param responseEnum
     * @return
     */
    public static Result setResult(ResponseEnum responseEnum){
        Result result = new Result();
        result.setCode(responseEnum.getCode());
        result.setMessage(responseEnum.getMessage());
        return  result;
    }

    /**
     * 设置接收数据 (重载实现接收集合和普通类型的不同处理)
     */
    public Result setData(String key,Object value){
        this.data.put(key,value);
        return this;
    }
    public Result setData(Map<String,Object> map){
        this.data=map;
        return this;
    }
    public Result setData(Set set){
        this.set=set;
        return this;
    }
    public Result setData(String s){
        this.content=s;
        return this;
    }
    public Result setData(String[] strings){
        this.strings=strings;
        return this;
    }

    /**
     * 设置特定响应消息
     */
    public Result setMsg(String message){
        this.setMessage(message);
        return this;
    }
    /**
     * 设置特定响应码
     */
    public Result setCod(Integer code){
        this.setCode(code);
        return this;
    }
}
