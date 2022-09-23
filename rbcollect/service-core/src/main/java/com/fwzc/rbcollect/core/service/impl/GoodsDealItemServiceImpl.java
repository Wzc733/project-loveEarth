package com.fwzc.rbcollect.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fwzc.common.util.LendNoUtils;
import com.fwzc.rbcollect.core.enums.CommonEnum;
import com.fwzc.rbcollect.core.mapper.*;
import com.fwzc.rbcollect.core.pojo.entity.*;
import com.fwzc.rbcollect.core.pojo.entity.query.GoodsDealItemQuery;
import com.fwzc.rbcollect.core.service.GoodsDealItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品购买下单订单 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2022-03-30
 */
@Service
public class GoodsDealItemServiceImpl extends ServiceImpl<GoodsDealItemMapper, GoodsDealItem> implements GoodsDealItemService {

    @Resource
    private TransFlowMapper transFlowMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserIntegralMapper userIntegralMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private UserLocalMapper userLocalMapper;
    @Resource
    private GoodsDealItemMapper goodsDealItemMapper;
    @Resource
    private GoodsDealItemService goodsDealItemService;


    @Override
    public IPage<GoodsDealItem> listPage(Long userId,Page<GoodsDealItem> pageParm, GoodsDealItemQuery goodsDealItemQuery) {
        QueryWrapper<GoodsDealItem> goodsDealItemQueryWrapper = new QueryWrapper<>();
        if (userId!=null){
            goodsDealItemQueryWrapper.eq("pay_user_id",userId);
        }
        goodsDealItemQueryWrapper.orderByDesc("create_time");
        if (goodsDealItemQuery == null) {
            Page<GoodsDealItem> dealItemPage = baseMapper.selectPage(pageParm, goodsDealItemQueryWrapper);
            List<GoodsDealItem> records = dealItemPage.getRecords();
            records.forEach(goodsDealItem -> {
                Goods goods = goodsMapper.selectById(goodsDealItem.getGoodsId());
                String goodsName = goods.getName();
                goodsDealItem.setGoodsName(goodsName);
                String msgByType = CommonEnum.getMsgByType(Integer.parseInt(goodsDealItem.getStatus()));
                goodsDealItem.setStatus(msgByType);
            });
            dealItemPage.setRecords(records);
            return dealItemPage;
        }

        Long payUserId = goodsDealItemQuery.getPayUserId();


        goodsDealItemQueryWrapper.eq(payUserId != null, "pay_user_id", payUserId);
        Page<GoodsDealItem> goodsDealItemPage = baseMapper.selectPage(pageParm, goodsDealItemQueryWrapper);
        List<GoodsDealItem> records = goodsDealItemPage.getRecords();
        records.forEach(goodsDealItem -> {
            Goods goods = goodsMapper.selectById(goodsDealItem.getGoodsId());
            String goodsName = goods.getName();
            goodsDealItem.setGoodsName(goodsName);
            String msgByType = CommonEnum.getMsgByType(Integer.parseInt(goodsDealItem.getStatus()));
            goodsDealItem.setStatus(msgByType);
        });
        goodsDealItemPage.setRecords(records);
        return goodsDealItemPage;

    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveGoodsDealItem(Long userId, Goods goods, UserInfo userInfo,Integer margin,Integer num) {
        try {

            QueryWrapper<UserIntegral> wrapperUserIntegral = new QueryWrapper<>();
            wrapperUserIntegral.eq("user_id", userId);
            UserIntegral userIntegral = userIntegralMapper.selectOne(wrapperUserIntegral);
            //减去商品所需的积分 更新到userIntegral表和userInfo表
            userIntegral.setIntegral(margin);
            userInfo.setIntegral(margin);

            //生成交易流水记录
            String userName = userInfo.getName();
            String goodsDealItemNo = LendNoUtils.getGoodsDealItemNo();
            TransFlow transFlow = new TransFlow();
            transFlow.setUserId(userId);
            transFlow.setUserName(userName);
            transFlow.setDealItemNo(goodsDealItemNo);
            transFlow.setTransType(CommonEnum.COMMODITY_TRADING.getType());
            transFlow.setTransAmount(-goods.getGoodsIntegral() * num);
            transFlow.setMemo(CommonEnum.BUY_GOODS.getMsg());
            transFlowMapper.insert(transFlow);

            //商品容量-1
            Integer volume = Integer.parseInt(goods.getVolume());
            Integer marginVolume = volume - num;
            goods.setVolume(marginVolume.toString());

            //生成商品交易记录
            //根据userLocalId获取地址详细信息
            String userLocalId = goods.getUserLocalId();
            UserLocal userLocal = userLocalMapper.selectById(goods.getUserLocalId());
            GoodsDealItem goodsDealItem = new GoodsDealItem();
            goodsDealItem.setDealItemNo(goodsDealItemNo);
            goodsDealItem.setPayUserId(userId);
            goodsDealItem.setPayUserName(userName);
            goodsDealItem.setStatus(CommonEnum.UN_FINISH.getType().toString());
            goodsDealItem.setDeliveryUserLocal(userLocal.getUserLocal());
            goodsDealItem.setDeliveryUserMobile(userLocal.getUserMobile());
            goodsDealItem.setDeliveryUserName(userLocal.getUserName());
            goodsDealItem.setGoodsId(goods.getId().toString());
            goodsDealItem.setGoodsName(goods.getName());
            goodsDealItem.setNums(num);

            //设置流水线记录id和用户地址id
            QueryWrapper<TransFlow> transFlowQueryWrapper = new QueryWrapper<>();
            transFlowQueryWrapper.eq("deal_item_no",goodsDealItemNo);
            TransFlow flow = transFlowMapper.selectOne(transFlowQueryWrapper);
            goodsDealItem.setTransFlowId(flow.getId());
            goodsDealItem.setUserLocalId((long) Integer.parseInt(userLocalId));

            goodsDealItemMapper.insert(goodsDealItem);
            goodsMapper.updateById(goods);
            userIntegralMapper.updateById(userIntegral);
            userInfoMapper.updateById(userInfo);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    @Override
    public List<GoodsDealItem> listFiveItem(Long userId) {

        QueryWrapper<GoodsDealItem> wrapper = new QueryWrapper<>();
        wrapper.eq("pay_user_id",userId).orderByDesc("update_time").last("limit 5");
        List<GoodsDealItem> goodsDealItems = baseMapper.selectList(wrapper);
        goodsDealItems.forEach(goodsDealItem -> {
            Goods goods = goodsMapper.selectById(goodsDealItem.getGoodsId());
            String goodsName = goods.getName();
            goodsDealItem.setGoodsName(goodsName);
            String msgByType = CommonEnum.getMsgByType(Integer.parseInt(goodsDealItem.getStatus()));
            goodsDealItem.setStatus(msgByType);
        });
        return goodsDealItems;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGoodsDeal(Integer goodsDealId) {
        try {
            QueryWrapper<GoodsDealItem> wrapper = new QueryWrapper<>();
            wrapper.eq("id", goodsDealId);
            GoodsDealItem goodsDealItem = baseMapper.selectOne(wrapper);
            //查询数据拼接给TransFlow对象
            Long goodsUserId = goodsDealItem.getPayUserId();
            String payUserName = goodsDealItem.getPayUserName();
            String goodsItemNo = goodsDealItem.getDealItemNo();

            TransFlow transFlow = new TransFlow();
            transFlow.setUserId(goodsUserId);
            transFlow.setUserName(payUserName);
            transFlow.setDealItemNo(goodsItemNo);

            transFlow.setTransType(CommonEnum.COMMODITY_TRADING.getType());
            transFlow.setMemo(CommonEnum.CANCEL_BUY_GOODS.getMsg());

            Goods goods = goodsMapper.selectById(goodsDealItem.getGoodsId());
            Integer nums = goodsDealItem.getNums();
            //容量返回
            Integer volumn = Integer.parseInt(goods.getVolume());
            Integer result = volumn + nums;
            goods.setVolume(result.toString());
            transFlow.setTransAmount( goods.getGoodsIntegral()*nums);
            //积分返回
            UserInfo userInfo = userInfoMapper.selectById(goodsDealItem.getPayUserId());
            userInfo.setIntegral( goods.getGoodsIntegral()*nums+userInfo.getIntegral());

            QueryWrapper<UserIntegral> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",goodsDealItem.getPayUserId());
            UserIntegral userIntegral = userIntegralMapper.selectOne(queryWrapper);
            userIntegral.setIntegral(goods.getGoodsIntegral() * goodsDealItem.getNums()+userIntegral.getIntegral());

            userInfoMapper.updateById(userInfo);
            userIntegralMapper.updateById(userIntegral);
            goodsMapper.updateById(goods);
            transFlowMapper.insert(transFlow);
            goodsDealItemMapper.deleteById(goodsDealId);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean sendGoods(Long id) {
        try {
            GoodsDealItem goodsDealItem = baseMapper.selectById(id);
            String dealItemNo = goodsDealItem.getDealItemNo();
            Long payUserId = goodsDealItem.getPayUserId();
            String payUserName = goodsDealItem.getPayUserName();

            goodsDealItem = new GoodsDealItem();
            goodsDealItem.setId(id);
            //修改订单状态
            goodsDealItem.setStatus(CommonEnum.IN_DELIVERY.getType().toString());


            //添加流水线
            TransFlow transFlow = new TransFlow();
            transFlow.setUserId(payUserId);
            transFlow.setUserName(payUserName);
            transFlow.setDealItemNo(dealItemNo);
            transFlow.setTransType(CommonEnum.COMMODITY_TRADING.getType());
            transFlow.setMemo(CommonEnum.CONFIRM_THE_DELIVARY_ADMIN.getMsg());

            baseMapper.updateById(goodsDealItem);
            transFlowMapper.insert(transFlow);
        }catch (Exception e){
            return false;
        }
        return true;

    }

    @Override
    public boolean tryDeleteGoodsDeal(Integer goodsDealId) {
        try {
            GoodsDealItem goodsDealItem = goodsDealItemMapper.selectById(goodsDealId);
            //状态修改
            goodsDealItem.setStatus(CommonEnum.IN_REVIEW.getType().toString());
            //用户申请取消
            goodsDealItem.setCancleCycle(true);

            //添加流水线
            Long rbUserId = goodsDealItem.getPayUserId();
            String rbUserName = goodsDealItem.getPayUserName();
            TransFlow transFlow = new TransFlow();
            transFlow.setUserId(rbUserId);
            transFlow.setUserName(rbUserName);
            transFlow.setDealItemNo(LendNoUtils.getRbDealItemNo());
            transFlow.setTransType(CommonEnum.COMMODITY_TRADING.getType());
            transFlow.setMemo(CommonEnum.CANCEL_BUY_GOODS.getMsg());



            transFlowMapper.insert(transFlow);
            baseMapper.updateById(goodsDealItem);
        }catch (Exception e){
            return false;
        }
        return true;

    }

    @Override
    public boolean removeGoodsById(long id) {
        try {
            QueryWrapper<GoodsDealItem> wrapper = new QueryWrapper<>();
            wrapper.eq("id",id);
            GoodsDealItem goodsDealItem = baseMapper.selectOne(wrapper);

            //查询数据拼接给TransFlow对象
            Long rbUserId = goodsDealItem.getPayUserId();
            String rbUserName = goodsDealItem.getPayUserName();
            String dealItemNo = goodsDealItem.getDealItemNo();

            TransFlow transFlow = new TransFlow();
            transFlow.setUserId(rbUserId);
            transFlow.setUserName(rbUserName);
            transFlow.setDealItemNo(dealItemNo);
            transFlow.setTransType(CommonEnum.COMMODITY_TRADING.getType());
            transFlow.setMemo(CommonEnum.CALCEL_THE_DELIVARY_ADMIN.getMsg());

            //容量返回
            Goods goods = goodsMapper.selectById(goodsDealItem.getGoodsId());
            Integer nums = goodsDealItem.getNums();
            Integer volumn = Integer.parseInt(goods.getVolume());
            Integer result = volumn + nums;
            goods.setVolume(result.toString());
            transFlow.setTransAmount( goods.getGoodsIntegral()*nums);
            //积分返回
            UserInfo userInfo = userInfoMapper.selectById(goodsDealItem.getPayUserId());
            userInfo.setIntegral( goods.getGoodsIntegral()*nums+userInfo.getIntegral());

            QueryWrapper<UserIntegral> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",goodsDealItem.getPayUserId());
            UserIntegral userIntegral = userIntegralMapper.selectOne(queryWrapper);
            userIntegral.setIntegral(goods.getGoodsIntegral() * goodsDealItem.getNums()+userIntegral.getIntegral());

            //用户申请取消2 管理员同意则直接删除
            baseMapper.deleteById(id);
            userInfoMapper.updateById(userInfo);
            userIntegralMapper.updateById(userIntegral);
            goodsMapper.updateById(goods);
            transFlowMapper.insert(transFlow);
        }catch (Exception e){
            return  false;
        }
        return true;

    }

    @Override
    public boolean DoneGoods(Long id) {
        try {
            GoodsDealItem goodsDealItem = goodsDealItemMapper.selectById(id);
            //状态修改
            goodsDealItem.setStatus(CommonEnum.All_FINISH.getType().toString());


            //添加流水线
            Long rbUserId = goodsDealItem.getPayUserId();
            String rbUserName = goodsDealItem.getPayUserName();
            TransFlow transFlow = new TransFlow();
            transFlow.setUserId(rbUserId);
            transFlow.setUserName(rbUserName);
            transFlow.setDealItemNo(LendNoUtils.getRbDealItemNo());
            transFlow.setTransType(CommonEnum.COMMODITY_TRADING.getType());
            transFlow.setMemo(CommonEnum.All_FINISH.getMsg());


            transFlowMapper.insert(transFlow);
            baseMapper.updateById(goodsDealItem);
        }catch (Exception e){
            return false;
        }
        return true;

    }

}