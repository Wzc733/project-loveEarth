package com.fwzc.rbcollect.core.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fwzc.common.exception.Assert;
import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.result.Result;
import com.fwzc.common.util.MD5;
import com.fwzc.common.util.RegexValidateUtils;
import com.fwzc.rbcollect.base.utils.JwtUtils;
import com.fwzc.rbcollect.core.enums.CommonEnum;
import com.fwzc.rbcollect.core.mapper.RbDealItemMapper;
import com.fwzc.rbcollect.core.mapper.UserInfoMapper;
import com.fwzc.rbcollect.core.pojo.entity.UserInfo;
import com.fwzc.rbcollect.core.pojo.entity.dto.RbDealChartsDTO;
import com.fwzc.rbcollect.core.pojo.entity.dto.UserIndexDTO;
import com.fwzc.rbcollect.core.pojo.entity.vo.LoginVO;
import com.fwzc.rbcollect.core.pojo.entity.vo.RegisterVO;
import com.fwzc.rbcollect.core.pojo.entity.vo.UserInfoDTO;
import com.fwzc.rbcollect.core.service.UserInfoService;
import com.fwzc.rbcollect.core.service.UserIntegralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2021-11-17
 */
@Api(tags = "会员接口")
@RestController
@RequestMapping("/api/core/userInfo")
@Slf4j
public class UserInfoController {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserIntegralService userIntegralService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RbDealItemMapper rbDealItemMapper;

    @ApiOperation("会员注册")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterVO registerVO){
        String mobile=registerVO.getMobile();
        String password=registerVO.getPassword();
        String code=registerVO.getCode();
        String email=registerVO.getEmail();
        String name=registerVO.getName();


        //表单校验
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notEmpty(code,ResponseEnum.CODE_NULL_ERROR);
        Assert.notEmpty(email,ResponseEnum.USER_EMAIL_NULL_ERROE);
        Assert.notEmpty(name,ResponseEnum.USER_NAME_NULL_ERROE);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);
        Assert.isTrue(RegexValidateUtils.checkEmail(email),ResponseEnum.USER_EMAIL_ERROE);

        //校验验证码是否正确
        String codeGen=(String)redisTemplate.opsForValue().get("rbcollect:sms:code:"+mobile);
        Assert.equals(code,codeGen, ResponseEnum.CODE_ERROR);

        //判断用户是否已被注册
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",registerVO.getMobile());
        Integer count = userInfoMapper.selectCount(userInfoQueryWrapper);
        Assert.isTrue(count==0, ResponseEnum.MOBILE_EXIST_ERROR);

        //注册
        userInfoService.register(registerVO);
        //用户积分表插入一条记录
        userIntegralService.insert(registerVO);
        return Result.ok().setMsg("注册成功");
    }

    @ApiOperation("会员登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVO loginVO, HttpServletRequest request){
        //校验
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        Assert.notEmpty(mobile,ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);
        //获取发送请求的主机ip
        String ip = request.getRemoteAddr();

        //用户是否存在
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        //对比的是数据库的字段名
        userInfoQueryWrapper.eq("mobile",mobile);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
        Assert.notNull(userInfo,ResponseEnum.LOGIN_MOBILE_ERROR);

        //密码是否正确
        Assert.equals(MD5.encrypt(password),userInfo.getPassword(),ResponseEnum.LOGIN_PASSWORD_ERROR);
        //用户是否被禁用
        Assert.equals(userInfo.getStatus(), CommonEnum.NORMAL_USER.getType(),ResponseEnum.LOGIN_LOKED_ERROR);

        UserInfoDTO userInfoDTO =userInfoService.login(loginVO,ip);

        if(userInfoDTO!=null){
            return Result.ok().setData("userInfo", userInfoDTO).setMsg("登陆成功");
        }else{
            return Result.error().setMsg("登陆失败");
        }
    }

    @ApiOperation("校验令牌")
    @GetMapping("/checkToken")
    public Result checkToken(HttpServletRequest request) {
        //前端把token存到请求头,然后前端请求该接口校验令牌
        String token = request.getHeader("token");
        boolean result = JwtUtils.checkToken(token);

        if(result){
            return Result.ok();
        }else{
            //LOGIN_AUTH_ERROR(-211, "未登录"),
            return Result.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }

    @ApiOperation("校验手机号是否注册")
    @GetMapping("/checkMobile/{mobile}")
    public boolean checkMobile(@PathVariable String mobile){
        return  userInfoService.checkMobile(mobile);
    }

    @ApiOperation("获取个人空间用户信息")
    @GetMapping("/auth/getIndexUserInfo")
    public Result getIndexUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        UserIndexDTO userIndexDTO = userInfoService.getIndexUserInfo(userId);
        return Result.ok().setData("userIndexVO", userIndexDTO);
    }

    @ApiOperation("保存会员头像")
    @GetMapping("/auth/save/img")
    public Result setImg(HttpServletRequest request,
                         @RequestParam("url") String url) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        String result= userInfoService.saveImg(userId,url);
        return Result.ok().setMsg(result);
    }

    @ApiOperation("获取用户人数")
    @GetMapping("getCountUser")
    public Result getCount() {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("status","1");
        Integer count = userInfoMapper.selectCount(wrapper);
        return Result.ok().setData("countUser",count);
    }

    @ApiOperation("根据时间获取垃圾回收次数")
    @GetMapping("/auth/getUserInfoForCharts")
    public Result getUserInfoForCharts(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        List<RbDealChartsDTO> rbDealForCharts =rbDealItemMapper.getRbDealForCharts(userId);
        int size = rbDealForCharts.size();
        List<String> counts = new ArrayList<>();
        List<String> times=new ArrayList<>();
        HashMap<String , String []> map = new HashMap<>();
        rbDealForCharts.forEach(userInfoForCharts -> {
            counts.add( userInfoForCharts.getCount());
            times.add(userInfoForCharts.getCreateTime());
        });
        String[] countsResult = counts.toArray(new String[size]);
        String[] timesResult = times.toArray(new String[size]);
        map.put("count",countsResult);
        map.put("time",timesResult);
        return Result.ok().setData("result", map);

    }

    @ApiOperation("获取用户状态")
    @GetMapping("/getStatusUser")
    public Result getStatusUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        UserInfo userInfo = userInfoMapper.selectById(userId);
        return Result.ok().setData("statusUser", CommonEnum.getMsgByType(userInfo.getStatus()));
    }

    @ApiOperation("更新用户")
    @PostMapping("/update")
    public Result updateById(
            @ApiParam(value = "用户对象",required = true)
            @RequestBody UserIndexDTO userIndexDTO,
            @ApiParam(value = "密码",required = true)
            @RequestParam("pwd") String password){

        String mobile=userIndexDTO.getPhone();
        String email=userIndexDTO.getEmail();
        String name=userIndexDTO.getName();
        //表单校验
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notEmpty(email,ResponseEnum.USER_EMAIL_NULL_ERROE);
        Assert.notEmpty(name,ResponseEnum.USER_NAME_NULL_ERROE);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);
        Assert.isTrue(RegexValidateUtils.checkEmail(email),ResponseEnum.USER_EMAIL_ERROE);

        boolean result=userInfoService.updateUser(userIndexDTO,password);
        if(result){
            return Result.ok().setMsg("更新成功").setData("result",true);
        }else {
            return Result.error().setMsg("更新失败").setData("result",false);
        }
    }

}

