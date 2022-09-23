package com.fwzc.rbcollect.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.core.mapper.UserFeedbackMapper;
import com.fwzc.rbcollect.core.pojo.entity.UserFeedback;
import com.fwzc.rbcollect.core.service.UserFeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * <p>
 * 用户反馈表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@Api(tags = "用户反馈接口")
@RestController
@RequestMapping("/admin/core/userFeedback")
public class AdminUserFeedbackController {

    @Resource
    private UserFeedbackService userFeedbackService;
    @Resource
    private UserFeedbackMapper userFeedbackMapper;

    @ApiOperation("已阅读用户反馈")
    @GetMapping("/haveRead")
    public Result haveRead(@ApiParam(value = "用户反馈记录id", required = true)
                                  @RequestParam("id") Integer feedBackId
                                  ) throws IOException {

            boolean result=userFeedbackService.haveRead(feedBackId);
            if (result){
                return Result.ok().setMsg("已阅读");
            }else {
                return Result.error().setMsg("阅读失败");
            }
    }

    @ApiOperation("获取用户反馈分页列表")
    @GetMapping("/list/{page}/{limit}")
    public Result listPage(
            @ApiParam(value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数",required = true)
            @PathVariable Long limit,
            @ApiParam(value = "查询对象",required = false)
                    UserFeedback userFeedback){
        //Page对象包含当前页码,每页记录数
        Page<UserFeedback> pageParm = new Page<>(page,limit);
        //查询出来的分页模型对象包含分页信息，查完的数据，有没有上一页下一页，一共多少条记录等全部有效信息
        IPage<UserFeedback> pageModel=userFeedbackService.listPage(pageParm,userFeedback);

        return Result.ok().setData("pageModel",pageModel);
    }

    @ApiOperation("获取用户反馈详细")
    @GetMapping("/getDetail")
    public Result getDetail(
            @ApiParam(value = "用户反馈id",required = true)
            @RequestParam Long id){
        UserFeedback userFeedback = userFeedbackMapper.selectById(id);

        return Result.ok().setData("userFeedback",userFeedback);
    }
}

