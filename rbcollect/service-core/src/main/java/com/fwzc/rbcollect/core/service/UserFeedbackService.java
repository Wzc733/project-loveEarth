package com.fwzc.rbcollect.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fwzc.rbcollect.core.pojo.entity.UserFeedback;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户反馈表 服务类
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
public interface UserFeedbackService extends IService<UserFeedback> {

    boolean saveFeedBacklItem(Long userId, UserFeedback userFeedback);

    boolean haveRead(Integer feedBackId);

    IPage<UserFeedback> listPage(Page<UserFeedback> pageParm, UserFeedback userFeedback);

}
