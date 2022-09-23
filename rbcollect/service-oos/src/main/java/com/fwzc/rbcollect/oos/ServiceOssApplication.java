package com.fwzc.rbcollect.oos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Auther:wzc
 * @Data:2021/11/29 - 11 - 29 - 14:34
 */
@SpringBootApplication
@ComponentScan({"com.fwzc.rbcollect", "com.fwzc.common"})
public class ServiceOssApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceOssApplication.class, args);
    }

}