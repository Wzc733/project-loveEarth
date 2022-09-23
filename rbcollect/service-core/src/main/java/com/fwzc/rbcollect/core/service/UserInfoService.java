package com.fwzc.rbcollect.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fwzc.rbcollect.core.pojo.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fwzc.rbcollect.core.pojo.entity.query.UserInfoQuery;
import com.fwzc.rbcollect.core.pojo.entity.vo.LoginVO;
import com.fwzc.rbcollect.core.pojo.entity.vo.RegisterVO;
import com.fwzc.rbcollect.core.pojo.entity.dto.UserIndexDTO;
import com.fwzc.rbcollect.core.pojo.entity.vo.UserInfoDTO;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
public interface UserInfoService extends IService<UserInfo> {

    IPage<UserInfo> listPage(Page<UserInfo> pageParm, UserInfoQuery userInfoQuery);

    int lock(Long id, Integer status);

    void register(RegisterVO registerVO);

    UserInfoDTO login(LoginVO loginVO, String ip);

    boolean checkMobile(String mobile);

    UserIndexDTO getIndexUserInfo(Long userId);

    String saveImg(Long userId, String url);

    boolean updateUser(UserIndexDTO userIndexDTO, String password);
}
