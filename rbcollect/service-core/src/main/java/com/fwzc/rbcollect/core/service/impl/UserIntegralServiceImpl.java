package com.fwzc.rbcollect.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fwzc.rbcollect.core.mapper.UserInfoMapper;
import com.fwzc.rbcollect.core.mapper.UserIntegralMapper;
import com.fwzc.rbcollect.core.pojo.entity.UserInfo;
import com.fwzc.rbcollect.core.pojo.entity.UserIntegral;
import com.fwzc.rbcollect.core.pojo.entity.vo.RegisterVO;
import com.fwzc.rbcollect.core.service.UserIntegralService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 用户积分记录表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@Service
public class UserIntegralServiceImpl extends ServiceImpl<UserIntegralMapper, UserIntegral> implements UserIntegralService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public void insert(RegisterVO registerVO) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",registerVO.getMobile());
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setUserId(userInfo.getId());
        userIntegral.setIntegral(0);
        baseMapper.insert(userIntegral);
    }
}
