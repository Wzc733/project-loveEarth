package com.fwzc.rbcollect.core.controller.api;


import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.base.utils.JwtUtils;
import com.fwzc.rbcollect.core.pojo.entity.UserFeedback;
import com.fwzc.rbcollect.core.service.UserFeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/core/userFeedback")
public class UserFeedbackController {
    @Resource
    private UserFeedbackService userFeedbackService;

    @ApiOperation("新增用户反馈")
    @PostMapping("/auth/save")
    public Result save(
            @ApiParam(value = "用户反馈对象", required = true)
            @RequestBody UserFeedback userFeedback,
            HttpServletRequest request) {

        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        boolean result = userFeedbackService.saveFeedBacklItem(userId, userFeedback);
        if(result==true){
            return Result.ok().setMsg("反馈成功");
        }else{
            return Result.error().setMsg("反馈失败");
        }
    }


}

