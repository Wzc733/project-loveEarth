package com.fwzc.rbcollect.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fwzc.rbcollect.core.pojo.entity.UserInfo;
import com.fwzc.rbcollect.core.pojo.entity.dto.UserInfoChartsDTO;

import java.util.List;

/**
 * <p>
 * 用户基本信息 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    List<UserInfoChartsDTO> getUserForCharts();


}
