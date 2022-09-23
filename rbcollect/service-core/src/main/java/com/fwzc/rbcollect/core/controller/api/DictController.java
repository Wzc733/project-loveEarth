package com.fwzc.rbcollect.core.controller.api;


import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.core.pojo.entity.Dict;
import com.fwzc.rbcollect.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@Api(tags = "数据字典查询接口")
@RestController
@RequestMapping("/api/core/dict")
@Slf4j
public class DictController {
    @Resource
    private DictService dictService;

    @ApiOperation("根据dictCode获取下级节点")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(
            @ApiParam(value = "节点编码",required = true)
            @PathVariable String dictCode){
        List<Dict> list=dictService.findByDictCode(dictCode);
        return Result.ok().setData("dictList",list);
    }

    @ApiOperation("根据上级id获取子节点数据列表")
    @GetMapping("/listByParentId")
    public Result listByParentId(
            @ApiParam(value = "上级节点id",required = true)
            @RequestParam Long parentId
    ){
        List<Dict> dictList=dictService.listByParentId(parentId);
        return Result.ok().setData("list",dictList);
    }

    @ApiOperation("根据上级名字name获取子节点数据列表")
    @GetMapping("/listByParentName")
    public Result listByParentName(
            @ApiParam(value = "上级节点name",required = true)
            @RequestParam String parentName
    ){
        List<Dict> dictList=dictService.listByParentName(parentName);
        return Result.ok().setData("list",dictList);
    }
}

