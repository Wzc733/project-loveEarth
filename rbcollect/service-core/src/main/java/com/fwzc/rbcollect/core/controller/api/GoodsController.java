package com.fwzc.rbcollect.core.controller.api;


import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.core.pojo.entity.Goods;
import com.fwzc.rbcollect.core.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Api(tags = "商品接口")
@RestController
@RequestMapping("/api/core/goods")
@Slf4j
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @ApiOperation("查询商品")
    @GetMapping("/selectGoods")
    public Result selectGoods(
            @ApiParam(value = "查询商品的名称",required = false)
                    String goodsName){

        List<Goods> goodsList=goodsService.selectGoods(goodsName);
        return Result.ok().setData("goodsList",goodsList);
    }
}

