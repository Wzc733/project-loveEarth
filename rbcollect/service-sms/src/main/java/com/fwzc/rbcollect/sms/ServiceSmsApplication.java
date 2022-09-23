package com.fwzc.rbcollect.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 王梓超
 * @Auther:wzc
 * @Data:2021/11/17 - 11 - 17 - 14:50
 */
@SpringBootApplication
@ComponentScan({"com.fwzc.rbcollect","com.fwzc.common"})  //扫描所有使用spring的包
@EnableFeignClients
public class ServiceSmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSmsApplication.class, args);
    }
}
