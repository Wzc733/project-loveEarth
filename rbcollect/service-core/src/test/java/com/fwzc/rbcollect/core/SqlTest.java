package com.fwzc.rbcollect.core;

import com.fwzc.rbcollect.core.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName : SqlTest
 * @Description : TODO
 * @Author : James
 * @Date : 2022/4/3 17:07
 * @Version 1.0
 */
public class SqlTest {

    @Autowired
    private UserInfoMapper userInfoMapper;

//    @Test
//    public void find(){
//        List<UserInfoCountDTO> userCountForTime = userInfoMapper.getUserCountForTime();
//        System.out.println(userCountForTime);
//    }
}
