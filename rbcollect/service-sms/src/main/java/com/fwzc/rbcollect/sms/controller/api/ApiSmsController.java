package com.fwzc.rbcollect.sms.controller.api;


import com.fwzc.common.exception.Assert;
import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.result.Result;
import com.fwzc.common.util.RandomUtils;
import com.fwzc.common.util.RegexValidateUtils;
import com.fwzc.rbcollect.sms.client_service.CoreUserInfoClient;
import com.fwzc.rbcollect.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Auther:wzc
 * @Data:2021/11/29 - 11 - 29 - 12:35
 */
@Api(tags = "短信管理")
@RestController
@RequestMapping("/api/sms")
@Slf4j
public class ApiSmsController {

    @Resource
    private SmsService smsService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private CoreUserInfoClient coreUserInfoClient;

    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    public Result send(
                       @ApiParam(value = "手机号",required = true)
                       @PathVariable String mobile){
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);

        //判断手机号是否注册  -----调用service-code服务
        boolean result = coreUserInfoClient.checkMobile(mobile);
        Assert.isTrue(result==false,ResponseEnum.MOBILE_EXIST_ERROR);

        HashMap<String, Object> map = new HashMap<>();
        String code = RandomUtils.getFourBitRandom();
        map.put("code",code);
        //存储到redis
        redisTemplate.opsForValue().set("rbcollect:sms:code:"+mobile,code,5, TimeUnit.MINUTES);

        smsService.send(mobile,map);
        return Result.ok().setMsg("短信发送成功");
    }
}
