package com.fwzc.rbcollect.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.core.enums.CommonEnum;
import com.fwzc.rbcollect.core.mapper.UserInfoMapper;
import com.fwzc.rbcollect.core.pojo.entity.UserInfo;
import com.fwzc.rbcollect.core.pojo.entity.dto.UserInfoChartsDTO;
import com.fwzc.rbcollect.core.pojo.entity.query.UserInfoQuery;
import com.fwzc.rbcollect.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 用户基本信息 获取和锁定/解锁
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@Api(tags = "用户管理")
@Slf4j
@RestController
@RequestMapping("/admin/core/userInfo")
public class AdminUserInfoController {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserInfoMapper userInfoMapper;

    @ApiOperation("获取用户分页列表")
    @GetMapping("/list/{page}/{limit}")
    public Result listPage(
            @ApiParam(value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数",required = true)
            @PathVariable Long limit,
            @ApiParam(value = "查询对象",required = false)
                    UserInfoQuery userInfoQuery){
        //Page对象包含当前页码,每页记录数
        Page<UserInfo> pageParm = new Page<>(page,limit);
        //查询出来的分页模型对象包含分页信息，查完的数据，有没有上一页下一页，一共多少条记录等全部有效信息
        IPage<UserInfo> pageModel=userInfoService.listPage(pageParm,userInfoQuery);

        return Result.ok().setData("pageModel",pageModel);
    }

    @ApiOperation("用户锁定与解锁")
    @GetMapping("/lock/{id}/{status}")
    public Result lock(
            @ApiParam(value = "用户id",required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "状态（111：锁定 112：正常）",required = true)
            @PathVariable("status") Integer status){
        int result = userInfoService.lock(id, status);
        if(result>0){
            return Result.ok().setMsg( CommonEnum.getMsgByType(status) );
        }else {
            return Result.error().setMsg("操作失败");
        }
    }

    @ApiOperation("根据时间获取用户人数")
    @GetMapping("/getUserInfoForCharts")
    public Result getUserInfoForCharts(){
        List<UserInfoChartsDTO> userForCharts = userInfoMapper.getUserForCharts();
        int size = userForCharts.size();
        List<String> counts = new ArrayList<>();
        List<String> times=new ArrayList<>();
        HashMap<String , String []> map = new HashMap<>();
        userForCharts.forEach(userInfoForCharts -> {
            counts.add( userInfoForCharts.getCount());
            times.add(userInfoForCharts.getCreateTime());
        });
        String[] countsResult = counts.toArray(new String[size]);
        String[] timesResult = times.toArray(new String[size]);
        map.put("count",countsResult);
        map.put("time",timesResult);
        return Result.ok().setData("result", map);

    }

}

