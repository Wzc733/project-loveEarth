package com.fwzc.rbcollect.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fwzc.rbcollect.core.mapper.UserLoginRecordMapper;
import com.fwzc.rbcollect.core.pojo.entity.UserLoginRecord;
import com.fwzc.rbcollect.core.service.UserLoginRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {
    @Override
    public List<UserLoginRecord> listTop50(Long userId) {
        QueryWrapper<UserLoginRecord> userLoginRecordQueryWrapper = new QueryWrapper<>();
        //orderByDesc 倒序查询(最近的)  last:往生成sql语句添加自定义sql语句
        userLoginRecordQueryWrapper.eq("user_id",userId)
                .orderByDesc("id")
                .last("limit 30");
        List<UserLoginRecord> userLoginRecords = baseMapper.selectList(userLoginRecordQueryWrapper);
        return userLoginRecords;
    }
}
