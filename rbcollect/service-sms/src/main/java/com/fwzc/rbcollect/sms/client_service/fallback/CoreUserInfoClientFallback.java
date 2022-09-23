package com.fwzc.rbcollect.sms.client_service.fallback;

import com.fwzc.rbcollect.sms.client_service.CoreUserInfoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 王梓超
 * @Auther:wzc
 * @Data:2021/12/3 - 12 - 03 - 20:54
 */
@Service
@Slf4j
public class CoreUserInfoClientFallback implements CoreUserInfoClient {
    @Override
    public boolean checkMobile(String mobile) {
        //如果远程调用失败,那么在这实现该服务
        log.error("远程调用失败,服务熔断");
        return false;
    }
}
