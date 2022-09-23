package com.fwzc.rbcollect.core.controller.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fwzc.common.exception.Assert;
import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.base.utils.JwtUtils;
import com.fwzc.rbcollect.core.enums.CommonEnum;
import com.fwzc.rbcollect.core.mapper.RbDealItemMapper;
import com.fwzc.rbcollect.core.mapper.UserInfoMapper;
import com.fwzc.rbcollect.core.pojo.entity.RbDealItem;
import com.fwzc.rbcollect.core.pojo.entity.UserInfo;
import com.fwzc.rbcollect.core.pojo.entity.dto.SortByImgDTO;
import com.fwzc.rbcollect.core.pojo.entity.dto.SortDTO;
import com.fwzc.rbcollect.core.service.RbDealItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 垃圾回收下单订单 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2022-03-30
 */
@Api(tags = "垃圾回收订单接口")
@RestController
@RequestMapping("/api/core/rbDealItem")
@Slf4j
public class RbDealItemController {

    @Resource
    private RbDealItemMapper rbDealItemMapper;
    @Resource
    private RbDealItemService rbDealItemService;
    @Resource
    private UserInfoMapper userInfoMapper;

    @ApiOperation("获取垃圾回收订单数")
    @GetMapping("getCountRb")
    public Result getCount() {
        QueryWrapper<RbDealItem> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "1");
        Integer count = rbDealItemMapper.selectCount(wrapper);
        return Result.ok().setData("countRb", count);
    }

    @ApiOperation("根据关键词获取垃圾分类")
    @GetMapping("/getRbSort")
    public Result getRbSort(@ApiParam(value = "关键词", required = true, example = "塑料袋")
                                @RequestParam("word") String word) throws IOException {
        SortDTO sortVO = rbDealItemService.getRbSort(word);
        return Result.ok().setData("RbSortResult", sortVO);
    }


    @ApiOperation("根据图片地址获取垃圾分类")
    @GetMapping("/getRbSortByImg")
    public Result getRbSortByImg(@ApiParam(value = "图片地址", required = true)
                                     @RequestParam("imgUrl") String imgUrl) throws IOException {
        SortByImgDTO sortByImgDTO = rbDealItemService.getRbSortByImg(imgUrl);
        if (sortByImgDTO != null) {
            return Result.ok().setData("RbSortResultByImg", sortByImgDTO);
        } else {
            return Result.error().setMsg("查询失败");
        }
    }

    @ApiOperation("新增垃圾回收交易")
    @PostMapping("/auth/save")
    public Result save(
            @ApiParam(value = "垃圾回收订单对象", required = true)
            @RequestBody RbDealItem rbDealItem,
            HttpServletRequest request) {

        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);

        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id",userId);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        //查询用户是否被锁定
        Assert.equals(userInfo.getStatus(), CommonEnum.NORMAL_USER.getType(), ResponseEnum.LOGIN_LOKED_ERROR);

        boolean result = rbDealItemService.saveRbDealItem(userId, userInfo,rbDealItem);
        if(result==true){
            return Result.ok().setMsg("垃圾回收申请成功");
        }else{
            return Result.error().setMsg("申请失败");
        }
    }

    @ApiOperation("查询用户垃圾回收订单前5条")
    @GetMapping("/auth/listFiveItem")
    public Result listFiveItem( HttpServletRequest request)  {
        //查询当前登陆用户的id
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Assert.notNull(userId,ResponseEnum.LOGIN_AUTH_ERROR);

        List<RbDealItem> rbDealItems = rbDealItemService.listFiveItem(userId);
        if (rbDealItems != null) {
            return Result.ok().setData("rbDealItemFiveItem", rbDealItems);
        } else {
            return Result.error().setMsg("查询失败");
        }
    }

    @ApiOperation("取消垃圾回收申请")
    @GetMapping("/auth/deleteRbDeal")
    public Result deleteRbDeal(@ApiParam(value = "垃圾回收申请记录id", required = true)
                            @RequestParam("id") Integer rbDealId,
                               HttpServletRequest request) throws IOException {
        String token = request.getHeader("token");
        if(JwtUtils.checkToken(token)){
            boolean result=rbDealItemService.deleteRbDeal(rbDealId);
            if (result){
                return Result.ok().setMsg("垃圾申请取消成功");
            }else {
                return Result.error().setMsg("取消失败");
            }
        }else {
            return Result.error().setMsg(ResponseEnum.LOGIN_AUTH_ERROR.getMessage());
        }
    }

    @ApiOperation("尝试取消垃圾回收申请")
    @GetMapping("/auth/tryDeleteRbDeal")
    public Result tryDeleteRbDeal(@ApiParam(value = "垃圾回收申请记录id", required = true)
                               @RequestParam("id") Integer rbDealId,
                               HttpServletRequest request) throws IOException {
        String token = request.getHeader("token");
        if(JwtUtils.checkToken(token)){
            boolean result=rbDealItemService.tryDeleteRbDeal(rbDealId);
            if (result){
                return Result.ok().setMsg("申请垃圾回收取消成功");
            }else {
                return Result.error().setMsg("申请失败");
            }
        }else {
            return Result.error().setMsg(ResponseEnum.LOGIN_AUTH_ERROR.getMessage());
        }
    }

    @ApiOperation("获取垃圾回收订单分页列表")
    @GetMapping("/auth/list/{page}/{limit}")
    public Result listPage(
            @ApiParam(value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数",required = true)
            @PathVariable Long limit,
            HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        if(JwtUtils.checkToken(token)){
            //Page对象包含当前页码,每页记录数
            Page<RbDealItem> pageParm = new Page<>(page,limit);
            //查询出来的分页模型对象包含分页信息，查完的数据，有没有上一页下一页，一共多少条记录等全部有效信息
            IPage<RbDealItem> pageModel=rbDealItemService.listPage(userId,pageParm,null);

            return Result.ok().setData("pageModel",pageModel);
        }else {
            return Result.error().setMsg(ResponseEnum.LOGIN_AUTH_ERROR.getMessage());
        }

    }
}

