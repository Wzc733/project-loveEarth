package com.fwzc.rbcollect.core.controller.admin;


import com.fwzc.common.exception.Assert;
import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.core.pojo.entity.Goods;
import com.fwzc.rbcollect.core.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品记录表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2022-03-07
 */
@Api(tags = "商品模块管理")
@Slf4j
@RestController
@RequestMapping("/admin/core/goods")
public class AdminGoodsController {

    @Resource
    private GoodsService goodsService;

    @ApiOperation("商品列表")
    @GetMapping("/list")
    public Result listAll(){
        List<Goods> list = goodsService.list();
        return Result.ok().setData("list",list).setMsg("获取列表成功");
    }

    @ApiOperation("新增商品")
    @PostMapping("/save")
    public Result save(
            @ApiParam(value = "商品对象",required = true)
            @RequestBody Goods goods){

        Assert.notNull(goods.getGoodsImg(), ResponseEnum.GOODS_IMG_NULL_ERROE);
        boolean result = goodsService.save(goods);
        if(result){
            return Result.ok().setMsg("保存成功");
        }else {
            return Result.error().setMsg("保存失败");
        }
    }

    @ApiOperation("根据id查询商品")
    @GetMapping("/get/{id}")
    public Result getById(
            @ApiParam(value = "数据id",required = true,example = "1")
            @PathVariable Long id){
        Goods goods = goodsService.getById(id);

        if(goods!=null){
            return Result.ok().setData("goodsQuery",goods).setMsg("查询成功");
        }else {
            return Result.error().setMsg("查询失败");
        }
    }
    @ApiOperation("更新商品")
    @PostMapping("/update")
    public Result updateById(
            @ApiParam(value = "商品对象",required = true)
            @RequestBody Goods goods){
        boolean result = goodsService.updateByIdwithAdd(goods);
        if(result){
            return Result.ok().setMsg("更新成功");
        }else {
            return Result.error().setMsg("更新失败");
        }
    }

    @ApiOperation(value = "根据id删除商品", notes = "逻辑删除")  //notes 进一步说明
    @DeleteMapping("/remove/{id}")
    public Result removeById(
            @ApiParam(value = "商品id", required = true, example = "1")   //example默认值
            @PathVariable long id){
        boolean result = goodsService.removeById(id);
        if(result){
            return Result.ok().setMsg("删除成功");
        }else {
            return Result.error().setMsg("删除失败");
        }
    }
}



