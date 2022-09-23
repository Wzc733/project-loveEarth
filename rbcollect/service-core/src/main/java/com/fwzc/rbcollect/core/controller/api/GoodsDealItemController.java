package com.fwzc.rbcollect.core.controller.api;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fwzc.common.exception.Assert;
import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.base.utils.JwtUtils;
import com.fwzc.rbcollect.core.config.CustomerBlockHandler;
import com.fwzc.rbcollect.core.enums.CommonEnum;
import com.fwzc.rbcollect.core.mapper.UserInfoMapper;
import com.fwzc.rbcollect.core.pojo.entity.Goods;
import com.fwzc.rbcollect.core.pojo.entity.GoodsDealItem;
import com.fwzc.rbcollect.core.pojo.entity.UserInfo;
import com.fwzc.rbcollect.core.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 1.获取锁并判断是否跟原来锁一致，
 * 2.一致，即将释放锁
 * 以上是以前释放分布式锁的步骤，有原子性但没有一致性
 * 可能因为阻塞，Redis超时释放了锁，然后别的线程拿到了锁
 * 接着继续运行，释放的就是别的线程的锁了。
 * 关键原因是，线程获取锁并判断是否一致和释放锁这两步不是同时执行。
 * 所以需要使用Lua脚本，在一个脚本编写多条Redis命令，可以同时执行。
 */

/**
 * <p>
 * 商品购买下单订单 前端控制器
 * </p>
 *
 * @author wzc
 * @since 2022-03-30
 */
@Api(tags = "商品交易订单接口")
@RestController
@RequestMapping("/api/core/goodsDealItem")
public class GoodsDealItemController {
    @Resource
    private UserIntegralService userIntegralService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private GoodsService goodsService;
    @Resource
    private GoodsDealItemService goodsDealItemService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private TransFlowService transFlowService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    @ApiOperation("新增商品交易")
    @PostMapping("/auth/save")
    @SentinelResource(value = "buyGoods",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handlerException1")
    public Result save(
            @ApiParam(value = "商品对象", required = true)
            @RequestBody Goods goods,
            @ApiParam(value = "交易数量", required = true)
            @RequestParam("num") Integer num,
            HttpServletRequest request) {

        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);

        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userId);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        Integer status = userInfo.getStatus();
        //查询用户是否被锁定
        Assert.equals(status, CommonEnum.NORMAL_USER.getType(), ResponseEnum.LOGIN_LOKED_ERROR);

        //查询用户积分是否足够
        Integer integral = userInfo.getIntegral();
        Integer goodsIntegral = goods.getGoodsIntegral();
        int margin = integral - goodsIntegral * num;
        Assert.isTrue(margin >= 0, ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);

        //查询用户有无地址
        String userLocalId = goods.getUserLocalId();
        Assert.notEmpty(userLocalId, ResponseEnum.LOCAL_IMG_NULL_ERROE);

        //使用分布式锁
        //每个线程的标识  ---value
        String uuid = UUID.randomUUID().toString();
        //--key 针对单个商品
        String lockKey = "lock:goods:" + goods.getId();
        boolean result = lock(lockKey, uuid, userId, goods, userInfo, margin, num);
        if (result) {
            return Result.ok().setMsg("购买成功");
        } else {
            return Result.error().setMsg("购买失败");
        }
    }

    private boolean lock(String lockKey, String uuid, Long userId, Goods goods, UserInfo userInfo, Integer margin, Integer num) {
        Boolean isLock = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, uuid, 30, TimeUnit.SECONDS);
        if (isLock) {
            //当获取到锁时
            try {
                //执行业务
                boolean result = goodsDealItemService.saveGoodsDealItem(userId, goods, userInfo, margin, num);
                if (result) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            } finally {
                //完成业务后,解锁
                stringRedisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(lockKey), uuid);
            }
        } else {
            //当没获取到锁时,自旋
            try {
                //休眠一秒
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (Exception e) {
                return false;
            }
            //重新尝试获取锁
            lock(lockKey, uuid, userId, goods, userInfo, margin, num);
        }
        return false;
    }

    @ApiOperation("查询商品订单前5条")
    @GetMapping("/auth/listFiveItem")
    public Result listFiveItem( HttpServletRequest request)  {
        //查询当前登陆用户的id
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Assert.notNull(userId, ResponseEnum.LOGIN_AUTH_ERROR);

        List<GoodsDealItem> goodsDealItems = goodsDealItemService.listFiveItem(userId);
        if (goodsDealItems != null) {
            return Result.ok().setData("goodsDealItemFiveItem", goodsDealItems);
        } else {
            return Result.error().setMsg("查询失败");
        }
    }

    @ApiOperation("获取商品交易订单分页列表")
    @GetMapping("/auth/list/{page}/{limit}")
    public Result listPage(
            @ApiParam(value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数",required = true)
            @PathVariable Long limit,
            HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        if (JwtUtils.checkToken(token)) {
            //Page对象包含当前页码,每页记录数
            Page<GoodsDealItem> pageParm = new Page<>(page, limit);
            //查询出来的分页模型对象包含分页信息，查完的数据，有没有上一页下一页，一共多少条记录等全部有效信息
            IPage<GoodsDealItem> pageModel = goodsDealItemService.listPage(userId,pageParm, null);

            return Result.ok().setData("pageModel", pageModel);
        } else {
            return Result.error().setMsg(ResponseEnum.LOGIN_AUTH_ERROR.getMessage());
        }

    }

    @ApiOperation("取消商品购买申请")
    @GetMapping("/auth/deleteGoodsDeal")
    public Result deleteRbDeal(@ApiParam(value = "商品购买记录id", required = true)
                               @RequestParam("id") Integer goodsDealId,
                               HttpServletRequest request) throws IOException {
        String token = request.getHeader("token");
        if(JwtUtils.checkToken(token)){

            boolean result=goodsDealItemService.deleteGoodsDeal(goodsDealId);
            if (result){
                return Result.ok().setMsg("商品购买取消成功");
            }else {
                return Result.error().setMsg("取消失败");
            }
        }else {
            return Result.error().setMsg(ResponseEnum.LOGIN_AUTH_ERROR.getMessage());
        }
    }

    @ApiOperation("用户尝试取消商品购买申请")
    @GetMapping("/auth/tryDeleteGoodsDeal")
    public Result tryDeleteRbDeal(@ApiParam(value = "商品购买记录id", required = true)
                                  @RequestParam("id") Integer goodsDealId,
                                  HttpServletRequest request) throws IOException {
        String token = request.getHeader("token");
        if(JwtUtils.checkToken(token)){
            boolean result=goodsDealItemService.tryDeleteGoodsDeal(goodsDealId);
            if (result){
                return Result.ok().setMsg("申请商品购买取消成功");
            }else {
                return Result.error().setMsg("申请失败");
            }
        }else {
            return Result.error().setMsg(ResponseEnum.LOGIN_AUTH_ERROR.getMessage());
        }
    }
}

