# project-loveEarth
个人毕设项目，由个人独立完成。
后端使用了SpringBoot开发接口，SpringCloud技术栈搭建微服务架构，包括open feign,nacos,spring cloud gataway,sentinel相关技术；整体微服务架构分为短信模块，核心功能模块，网关模块，存储系统模块。使用MyBatis-Plus进行数据库交互和代码生成器生成代码，Redis设置缓存和设置分布式锁与配合Lua脚本解锁，包括一些自定义异常处理类，响应枚举类，断言，MD5加密，JWT验证。后台接口使用swagger测试。 
前端使用Element-UI和Echars作为前端组件，基于Vue.js语法的Nuxt.js前端框架搭建前端项目和使用开源项目vue-element-admin作为后台管理系统。 该系统还应用了一些三方接口，主要有短信网关，阿里云OSS，EasyExcel，图片识别接口。       
爱地球回收站主要实现了垃圾图片与文字识别，申请垃圾上门，奖励机制，商城购买，人员商品等一系列管理等功能。
