package com.fwzc.rbcollect.sms.service;

import java.util.Map;

/**
 * @Auther:wzc
 * @Data:2021/11/28 - 11 - 28 - 18:26
 */
public interface SmsService {
    void send(String mobile, Map<String, Object> param);
}
