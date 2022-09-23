package com.fwzc.rbcollect.core.service;

import com.fwzc.rbcollect.core.pojo.entity.UserIntegral;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fwzc.rbcollect.core.pojo.entity.vo.RegisterVO;

/**
 * <p>
 * 用户积分记录表 服务类
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
public interface UserIntegralService extends IService<UserIntegral> {

    void insert(RegisterVO registerVO);

}
