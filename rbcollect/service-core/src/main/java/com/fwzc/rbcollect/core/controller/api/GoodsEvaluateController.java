package com.fwzc.rbcollect.core.controller.api;


import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.base.utils.JwtUtils;
import com.fwzc.rbcollect.core.pojo.entity.GoodsEvaluate;
import com.fwzc.rbcollect.core.pojo.entity.dto.GoodsEvaluateDTO;
import com.fwzc.rbcollect.core.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 商品评价表 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2022-04-15
 */
@Api(tags = "商品评价接口")
@RestController
@RequestMapping("/api/core/goodsEvaluate")
public class GoodsEvaluateController {
    @Resource
    private UserIntegralService userIntegralService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private GoodsDealItemService goodsDealItemService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private TransFlowService transFlowService;
    @Resource
    private GoodsEvaluateService goodsEvaluateService;

    @ApiOperation("新增商品评价记录")
    @PostMapping("/auth/save")
    public Result save(
            @ApiParam(value = "商品评价对象", required = true)
            @RequestBody GoodsEvaluate goodsEvaluate,
            HttpServletRequest request) {

        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        boolean result = goodsEvaluateService.savegoodsEvaluate(goodsEvaluate,userId);
        if(result==true){
            return Result.ok().setMsg("商品评价成功");
        }else{
            return Result.error().setMsg("申请失败");
        }
    }


    @ApiOperation("获取用户评价的前50条")
    @GetMapping("/listFiftyEvaluate")
    public Result listFiftyEvaluate(@ApiParam(value = "商品Id", required = true)
                                        @RequestParam("id") String goodsId)  {
        List<GoodsEvaluateDTO> goodsEvaluates=goodsEvaluateService.listFiftyEvaluate(goodsId);
        return Result.ok().setData("listFiftyEvaluate",goodsEvaluates);
    }
}

