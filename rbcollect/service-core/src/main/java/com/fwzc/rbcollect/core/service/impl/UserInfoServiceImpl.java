package com.fwzc.rbcollect.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fwzc.common.exception.Assert;
import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.util.MD5;
import com.fwzc.rbcollect.base.utils.JwtUtils;
import com.fwzc.rbcollect.core.enums.CommonEnum;
import com.fwzc.rbcollect.core.mapper.UserInfoMapper;
import com.fwzc.rbcollect.core.mapper.UserIntegralMapper;
import com.fwzc.rbcollect.core.mapper.UserLocalMapper;
import com.fwzc.rbcollect.core.mapper.UserLoginRecordMapper;
import com.fwzc.rbcollect.core.pojo.entity.UserInfo;
import com.fwzc.rbcollect.core.pojo.entity.UserIntegral;
import com.fwzc.rbcollect.core.pojo.entity.UserLocal;
import com.fwzc.rbcollect.core.pojo.entity.UserLoginRecord;
import com.fwzc.rbcollect.core.pojo.entity.dto.UserIndexDTO;
import com.fwzc.rbcollect.core.pojo.entity.query.UserInfoQuery;
import com.fwzc.rbcollect.core.pojo.entity.vo.LoginVO;
import com.fwzc.rbcollect.core.pojo.entity.vo.RegisterVO;
import com.fwzc.rbcollect.core.pojo.entity.vo.UserInfoDTO;
import com.fwzc.rbcollect.core.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserLoginRecordMapper userLoginRecordMapper;
    @Resource
    private  UserIntegralMapper userIntegralMapper;
    @Resource
    private UserLocalMapper userLocalMapper;

    @Override
    public IPage<UserInfo> listPage(Page<UserInfo> pageParm, UserInfoQuery userInfoQuery) {

        String mobile = userInfoQuery.getMobile();
        Integer status = userInfoQuery.getStatus();

        if(userInfoQuery==null){
            baseMapper.selectPage(pageParm,null);
        }
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();

//        userInfoQueryWrapper.eq("mobile",mobile)
//                            .eq("status",status)
//                            .eq("user_type",userType)

        //用户不一定全部属性都输入查询条件  所以分开写
        //该方法查询不为空 不是空字符串 不包含空字符串
//        if (StringUtils.isNotBlank(mobile)){
//            userInfoQueryWrapper.eq("mobile",mobile);
//        }
//        if (status!=null){
//            userInfoQueryWrapper .eq("status",status);
//        }
//        if (userType!=null){
//            userInfoQueryWrapper.eq("user_type",userType);
//        }

        userInfoQueryWrapper.eq(StringUtils.isNotBlank(mobile),"mobile",mobile)
                .eq(status!=null,"status",status);
        return baseMapper.selectPage(pageParm,userInfoQueryWrapper);
    }

    @Override
    public int lock(Long id, Integer status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(status);
        int result = baseMapper.updateById(userInfo);
        return result;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterVO registerVO) {

        //插入用户信息记录  user_info
        UserInfo userInfo = new UserInfo();
        userInfo.setName(registerVO.getName());
        userInfo.setMobile(registerVO.getMobile());
        userInfo.setEmail(registerVO.getEmail());
        userInfo.setPassword(MD5.encrypt(registerVO.getPassword()));
        userInfo.setStatus(CommonEnum.NORMAL_USER.getType());

        baseMapper.insert(userInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoDTO login(LoginVO loginVO, String ip) {
        //获取前端传入的LoginVO对象
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();

        //用户是否存在
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        //对比的是数据库的字段名
        userInfoQueryWrapper.eq("mobile",mobile);
        UserInfo userInfo = baseMapper.selectOne(userInfoQueryWrapper);
        Assert.notNull(userInfo,ResponseEnum.LOGIN_MOBILE_ERROR);

        //密码是否正确
        Assert.equals(MD5.encrypt(password),userInfo.getPassword(),ResponseEnum.LOGIN_PASSWORD_ERROR);
        //用户是否被禁用
        Assert.equals(userInfo.getStatus(), CommonEnum.NORMAL_USER.getType(),ResponseEnum.LOGIN_LOKED_ERROR);
        //记录登录日志
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(ip);
        userLoginRecordMapper.insert(userLoginRecord);
        //生成token
        String token = JwtUtils.createToken(userInfo.getId(), userInfo.getName());

        //组装userInfoVO 返回给前端使用
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setToken(token);
        QueryWrapper<UserInfo> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.eq("mobile",loginVO.getMobile());
        UserInfo userInfo1 = baseMapper.selectOne(userInfoWrapper);
        userInfoDTO.setName(userInfo1.getName());
        userInfoDTO.setHeadImg(userInfo1.getHeadImg());
        userInfoDTO.setMobile(mobile);
        //返回
        return userInfoDTO;
    }

    @Override
    public boolean checkMobile(String mobile) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(userInfoQueryWrapper);
        return  count>0;
    }

    @Override
    public UserIndexDTO getIndexUserInfo(Long userId) {
        UserInfo userInfo = baseMapper.selectById(userId);

        QueryWrapper<UserIntegral> userIntegralQueryWrapper = new QueryWrapper<>();
        userIntegralQueryWrapper.eq("user_id",userId);
        UserIntegral userIntegral = userIntegralMapper.selectOne(userIntegralQueryWrapper);

        QueryWrapper<UserLoginRecord> userLoginRecordQueryWrapper = new QueryWrapper<>();
        userLoginRecordQueryWrapper.eq("user_id",userId)
                .orderByDesc("id")
                .last("limit 1");
        UserLoginRecord userLoginRecord = userLoginRecordMapper.selectOne(userLoginRecordQueryWrapper);

        QueryWrapper<UserLocal> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        Integer count = userLocalMapper.selectCount(wrapper);

        UserIndexDTO userIndexDTO = new UserIndexDTO();
        userIndexDTO.setId(userId);
        userIndexDTO.setName(userInfo.getName());
        userIndexDTO.setHeadImg(userInfo.getHeadImg());
        userIndexDTO.setStatus(userInfo.getStatus());
        userIndexDTO.setPhone(userInfo.getMobile());
        userIndexDTO.setEmail(userInfo.getEmail());
        userIndexDTO.setBindLocal(count>0);
        userIndexDTO.setIntegral(userIntegral.getIntegral());
        userIndexDTO.setLastLoginTime(userLoginRecord.getCreateTime());

        return userIndexDTO;
    }

    @Override
    public String saveImg(Long userId, String imgUrl) {
        UserInfo userInfo = baseMapper.selectById(userId);
        userInfo.setHeadImg(imgUrl);
        baseMapper.updateById(userInfo);
        return "头像保存成功";
    }

    @Override
    public boolean updateUser(UserIndexDTO userIndexDTO, String password) {
        String email = userIndexDTO.getEmail();
        String headImg = userIndexDTO.getHeadImg();
        String phone = userIndexDTO.getPhone();
        String name = userIndexDTO.getName();
        Long id = userIndexDTO.getId();

        UserInfo userInfo = baseMapper.selectById(id);
        userInfo.setEmail(email);
        userInfo.setHeadImg(headImg);
        userInfo.setMobile(phone);
        userInfo.setName(name);
        if (password!=null){
            userInfo.setPassword(MD5.encrypt(password));
        }
        int i = baseMapper.updateById(userInfo);
        if (i>0){
            return true;
        }else {
            return false;
        }

    }
}
