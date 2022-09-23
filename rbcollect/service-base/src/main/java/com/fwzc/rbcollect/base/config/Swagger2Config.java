package com.fwzc.rbcollect.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Auther:wzc
 * @Data:2021/11/17 - 11 - 17 - 15:46
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket adminApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                //1.给不同用户类型的接口分组，方便管理
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                //2.设置过滤器,给不用接口分配到不同组。方法：设置前缀
                .select()
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }

    @Bean
    public Docket apiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("api")
                .apiInfo(webApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();

    }
    private ApiInfo adminApiInfo(){
        return new ApiInfoBuilder().title("垃圾回收后台管理系统API文档")
                .description("本文档描述了后台管理系统各个模块的接口的调用方式")
                .contact(new Contact("wzc","1274419801@qq.com","13592881981"))
                .build();
    }
    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder().title("垃圾回收网址站管理系统API文档")
                .description("本文档描述了网站各个模块的接口的调用方式")
                .contact(new Contact("wzc","1274419801@qq.com","13592881981"))
                .build();
    }
}
