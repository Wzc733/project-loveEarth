package com.fwzc.rbcollect.core.controller.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fwzc.common.exception.Assert;
import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.base.utils.JwtUtils;
import com.fwzc.rbcollect.core.mapper.UserInfoMapper;
import com.fwzc.rbcollect.core.mapper.UserLocalMapper;
import com.fwzc.rbcollect.core.pojo.entity.UserInfo;
import com.fwzc.rbcollect.core.pojo.entity.UserLocal;
import com.fwzc.rbcollect.core.pojo.entity.dto.UserLocalDTO;
import com.fwzc.rbcollect.core.service.UserLocalService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户地址表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@RestController
@RequestMapping("/api/core/userLocal")
public class UserLocalController {

    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserLocalService userLocalService;
    @Resource
    private UserLocalMapper userLocalMapper;

    @ApiOperation("获取用户的姓名和号码填充至前端页面")
    @GetMapping("/auth/getUserInfoConnectLocal")
    public Result getIndexUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        UserInfo userInfo = userInfoMapper.selectById(userId);
        String name = userInfo.getName();
        String mobile = userInfo.getMobile();
        Long id = userInfo.getId();

        UserLocal userLocal = new UserLocal();
        userLocal.setUserId(id.toString());
        userLocal.setUserMobile(mobile);
        userLocal.setUserName(name);
        return Result.ok().setData("userLocal", userLocal);
    }


    @ApiOperation("新增用户地址")
    @PostMapping("/save")
    public Result save(
            @ApiParam(value = "用户地址对象",required = true)
            @RequestBody  UserLocal userLocal){

        Assert.notNull(userLocal.getUserLocal(), ResponseEnum.USER_LOCAL_NULL_ERROR);
        boolean result = userLocalService.save(userLocal);
        if(result){
            return Result.ok().setMsg("保存成功");
        }else {
            return Result.error().setMsg("保存失败");
        }
    }

    @ApiOperation("获取用户的姓名,号码,地址拼接成字符串传给前端")
    @GetMapping("/auth/getUserLocalWithString")
    public Result getUserLocalWithString(HttpServletRequest request
                                         ) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);

        QueryWrapper<UserLocal> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        //获取当前登陆用户登记的所有地址
        List<UserLocal> userLocals = userLocalMapper.selectList(wrapper);

        List<UserLocalDTO> userLocalDTOS = new ArrayList<>();
        userLocals.forEach(userLocal -> {
            UserLocalDTO userLocalDTO = new UserLocalDTO();
            userLocalDTO.setId(userLocal.getId().toString());
            userLocalDTO.setLocal(userLocal.getUserName()+" - "+userLocal.getUserLocal()+" - "+userLocal.getUserMobile());
            userLocalDTOS.add(userLocalDTO);
        });
        return Result.ok().setData("userLocalDTOList", userLocalDTOS);

    }

    @ApiOperation("获取用户地址对象")
    @GetMapping("/auth/getUserLocal")
    public Result getUserLocal(HttpServletRequest request
                                ,@ApiParam(value = "用户地址id", required = false)
                                @RequestParam(value = "id",required = false) Integer userLocalId) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        if(userLocalId!=null){
            UserLocal userLocal = userLocalMapper.selectById(userLocalId);
            return Result.ok().setData("userLocal",userLocal);
        }else {
        QueryWrapper<UserLocal> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        //获取当前登陆用户登记的所有地址
        List<UserLocal> userLocals = userLocalMapper.selectList(wrapper);

        return Result.ok().setData("userLocalList", userLocals);
        }
    }

    @ApiOperation("删除用户地址对象")
    @GetMapping("/deleteUserLocal")
    public Result deleteUserLocal(@ApiParam(value = "用户地址对象id", required = true)
                                      @RequestParam("id") Integer localId) {

        int result = userLocalMapper.deleteById(localId);
        if (result>0){
            return Result.ok().setMsg("删除成功");
        }else {
            return Result.error().setMsg("删除失败");
        }

    }

    @ApiOperation("更新用户地址对象")
    @PostMapping("/auth/updateUserLocal")
    public Result updateUserLocal(@ApiParam(value = "用户地址对象", required = true)
                                      @RequestBody UserLocal userLocal) {
        int i = userLocalMapper.updateById(userLocal);
        if (i>0){
            return Result.ok().setMsg("更新成功");
        }else {
            return Result.error().setMsg("更新失败");
        }
    }
}

