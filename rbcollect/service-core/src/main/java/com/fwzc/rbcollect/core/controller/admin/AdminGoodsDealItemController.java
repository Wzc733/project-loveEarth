package com.fwzc.rbcollect.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.core.pojo.entity.GoodsDealItem;
import com.fwzc.rbcollect.core.pojo.entity.query.GoodsDealItemQuery;
import com.fwzc.rbcollect.core.service.GoodsDealItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 商品购买下单订单 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2022-03-30
 */
@Api(tags = "商品交易管理")
@Slf4j
@RestController
@RequestMapping("/admin/core/goodsDealItem")
public class AdminGoodsDealItemController {
    @Resource
    private GoodsDealItemService goodsDealItemService;

    @ApiOperation("获取商品交易分页列表")
    @GetMapping("/list/{page}/{limit}")
    public Result listPage(
            @ApiParam(value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数",required = true)
            @PathVariable Long limit,
            @ApiParam(value = "查询对象",required = false)
                    GoodsDealItemQuery goodsDealItemQuery){
        //Page对象包含当前页码,每页记录数
        Page<GoodsDealItem> pageParm = new Page<>(page,limit);
        //查询出来的分页模型对象包含分页信息，查完的数据，有没有上一页下一页，一共多少条记录等全部有效信息
        IPage<GoodsDealItem> pageModel=goodsDealItemService.listPage(null,pageParm,goodsDealItemQuery);

        return Result.ok().setData("pageModel",pageModel);
    }

    @ApiOperation("商品发货")
    @GetMapping("/sendGoods/{id}")
    public Result sendGoods(
            @ApiParam(value = "商品交易记录id",required = true)
            @PathVariable("id") Long id){
        boolean result=goodsDealItemService.sendGoods(id);
        if (result){
            return Result.ok().setMsg("管理员发货成功");
        }else {
            return Result.error().setMsg("发货失败");
        }
    }

    @ApiOperation(value = "根据id删除商品交易", notes = "逻辑删除")  //notes 进一步说明
    @DeleteMapping("/remove/{id}")
    public Result removeById(
            @ApiParam(value = "数据id", required = true, example = "100")   //example默认值
            @PathVariable long id){
        boolean result = goodsDealItemService.removeGoodsById(id);
        if(result){
            return Result.ok().setMsg("删除成功");
        }else {
            return Result.error().setMsg("删除失败");
        }
    }

    @ApiOperation("管理员完成商品交易")
    @GetMapping("/doneGoods/{id}")
    public Result doneGoods(
            @ApiParam(value = "商品交易记录id",required = true)
            @PathVariable("id") Long id){
        boolean result=goodsDealItemService.DoneGoods(id);
        if (result){
            return Result.ok().setMsg("商品交易完成");
        }else {
            return Result.error().setMsg("该操作失败");
        }

    }
}

