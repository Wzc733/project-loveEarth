package com.fwzc.rbcollect.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fwzc.rbcollect.core.enums.CommonEnum;
import com.fwzc.rbcollect.core.mapper.UserFeedbackMapper;
import com.fwzc.rbcollect.core.pojo.entity.UserFeedback;
import com.fwzc.rbcollect.core.service.UserFeedbackService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户反馈表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@Service
public class UserFeedbackServiceImpl extends ServiceImpl<UserFeedbackMapper, UserFeedback> implements UserFeedbackService {

    @Override
    public boolean saveFeedBacklItem(Long userId, UserFeedback userFeedback) {
        try {
            UserFeedback userFeedback1 = new UserFeedback();
            BeanUtils.copyProperties(userFeedback,userFeedback1);
            userFeedback1.setUserId(userId.toString());
            userFeedback1.setStatus(CommonEnum.NO_SEE.getType().toString());
            baseMapper.insert(userFeedback1);
        }catch (Exception e){
            return false;
        }
        return true;

    }

    @Override
    public boolean haveRead(Integer feedBackId) {
        try {
            UserFeedback userFeedback = baseMapper.selectById(feedBackId);
            userFeedback.setStatus(CommonEnum.DONE_SEE.getType().toString());
            baseMapper.updateById(userFeedback);
        }catch (Exception e){
            return false;
        }
        return true;

    }

    @Override
    public IPage<UserFeedback> listPage(Page<UserFeedback> pageParm, UserFeedback userFeedback) {

        if(userFeedback==null||(userFeedback.getStatus()==null&&userFeedback.getFeedbackType()==null)){
            Page<UserFeedback> userFeedbackPage = baseMapper.selectPage(pageParm, null);
            userFeedbackPage.getRecords().forEach(userFeedback1 -> {
                userFeedback1.setFeedbackType(CommonEnum.getMsgByType(Integer.parseInt(userFeedback1.getFeedbackType())));
                userFeedback1.setStatus(CommonEnum.getMsgByType(Integer.parseInt(userFeedback1.getStatus())));
            });
            return userFeedbackPage;
        }
        QueryWrapper<UserFeedback> wrapper = new QueryWrapper<>();
        String status = userFeedback.getStatus();
        String feedbackType = userFeedback.getFeedbackType();
        wrapper.eq(feedbackType!=null,"feedback_type",feedbackType)
                .eq(status!=null,"status",status);
        Page<UserFeedback> userFeedbackPage = baseMapper.selectPage(pageParm, wrapper);
        userFeedbackPage.getRecords().forEach(userFeedback1 -> {
            userFeedback1.setFeedbackType(CommonEnum.getMsgByType(Integer.parseInt(userFeedback1.getFeedbackType())));
            userFeedback1.setStatus(CommonEnum.getMsgByType(Integer.parseInt(userFeedback1.getStatus())));
        });
        return userFeedbackPage;
    }
}
