package com.fwzc.rbcollect.core.controller.admin;


import com.fwzc.common.exception.Assert;
import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.core.pojo.entity.IntegralGrade;
import com.fwzc.rbcollect.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户积分记录表 CRUD
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@Slf4j
@Api(tags = "积分等级管理")
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminUserIntegralController {
    @Resource
    private IntegralGradeService integralGradeService;

    @ApiOperation("积分等级列表")
    @GetMapping("/list")
    public Result listAll(){
        List<IntegralGrade> list = integralGradeService.list();
        return Result.ok().setData("list",list).setMsg("获取列表成功");
    }

    @ApiOperation(value = "根据id删除积分等级", notes = "逻辑删除")  //notes 进一步说明
    @DeleteMapping("/remove/{id}")
    public Result removeById(
            @ApiParam(value = "数据id", required = true, example = "100")   //example默认值
            @PathVariable long id){
        boolean result = integralGradeService.removeById(id);
        if(result){
            return Result.ok().setMsg("删除成功");
        }else {
            return Result.error().setMsg("删除失败");
        }
    }

    @ApiOperation("根据id查询积分等级")
    @GetMapping("/get/{id}")
    public Result getById(
            @ApiParam(value = "数据id",required = true,example = "1")
            @PathVariable Long id){
        IntegralGrade integralGrade=integralGradeService.getById(id);

        if(integralGrade!=null){
            return Result.ok().setData("record",integralGrade).setMsg("查询成功");
        }else {
            return Result.error().setMsg("查询失败");
        }
    }

    @ApiOperation("新增积分等级")
    @PostMapping("/save")
    public Result save(
            @ApiParam(value = "积分等级对象",required = true)
            @RequestBody IntegralGrade integralGrade){

        Assert.notNull(integralGrade.getIntegralAmount(), ResponseEnum.USER_INTERGRAL_NULL_ERROE);
        boolean result = integralGradeService.save(integralGrade);
        if(result){
            return Result.ok().setMsg("保存成功");
        }else {
            return Result.error().setMsg("保存失败");
        }
    }


    @ApiOperation("更新积分等级")
    @PostMapping("/update")
    public Result updateById(
            @ApiParam(value = "积分等级对象",required = true)
            @RequestBody IntegralGrade integralGrade){
        boolean result = integralGradeService.updateById(integralGrade);
        if(result){
            return Result.ok().setMsg("更新成功");
        }else {
            return Result.error().setMsg("更新失败");
        }
    }
}

