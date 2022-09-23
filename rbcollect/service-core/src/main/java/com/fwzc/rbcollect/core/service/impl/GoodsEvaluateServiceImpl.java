package com.fwzc.rbcollect.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fwzc.rbcollect.core.mapper.GoodsDealItemMapper;
import com.fwzc.rbcollect.core.mapper.GoodsEvaluateMapper;
import com.fwzc.rbcollect.core.mapper.UserInfoMapper;
import com.fwzc.rbcollect.core.pojo.entity.GoodsDealItem;
import com.fwzc.rbcollect.core.pojo.entity.GoodsEvaluate;
import com.fwzc.rbcollect.core.pojo.entity.UserInfo;
import com.fwzc.rbcollect.core.pojo.entity.dto.GoodsEvaluateDTO;
import com.fwzc.rbcollect.core.service.GoodsEvaluateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品评价表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2022-04-15
 */
@Service
public class GoodsEvaluateServiceImpl extends ServiceImpl<GoodsEvaluateMapper, GoodsEvaluate> implements GoodsEvaluateService {

    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private GoodsDealItemMapper goodsDealItemMapper;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savegoodsEvaluate(GoodsEvaluate goodsEvaluate, Long userId) {
        try {
            //查看 该评价对应的订单号是否存在
            String dealItemNo = goodsEvaluate.getDealItemNo();
            QueryWrapper<GoodsEvaluate> wrapper = new QueryWrapper<>();
            Integer count = baseMapper.selectCount(wrapper);
            if(count>0){
                //如果该评价存在 说明已经有1条评价了
                //则将dealitem的EvaluateFull字段设为true
                QueryWrapper<GoodsDealItem> wrapper1 = new QueryWrapper<>();
                wrapper1.eq("deal_item_no",dealItemNo);
                GoodsDealItem goodsDealItem = goodsDealItemMapper.selectOne(wrapper1);
                goodsDealItem.setEvaluateFull(true);
                goodsDealItemMapper.updateById(goodsDealItem);
            }
            //查看用户评价
            GoodsEvaluate goodsEvaluate1 = new GoodsEvaluate();
            BeanUtils.copyProperties(goodsEvaluate,goodsEvaluate1);
            UserInfo userInfo = userInfoMapper.selectById(userId);
            goodsEvaluate1.setUserId(Math.toIntExact(userId));
            goodsEvaluate1.setUserName(userInfo.getName());
            baseMapper.insert(goodsEvaluate1);
        }catch (Exception e){
            return false;
        }
        return true;

    }

    @Override
    public List<GoodsEvaluateDTO> listFiftyEvaluate(String goodsId) {
        QueryWrapper<GoodsEvaluate> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id",goodsId).orderByDesc("update_time").last("limit 50");
        List<GoodsEvaluate> goodsEvaluates = baseMapper.selectList(wrapper);
        List<GoodsEvaluateDTO> evaluateDTOS = new ArrayList<>();
        //拼装数据给DTO对象 返回给前端使用
        goodsEvaluates.forEach(goodsEvaluate -> {
            GoodsEvaluateDTO goodsEvaluateDTO = new GoodsEvaluateDTO();
            BeanUtils.copyProperties(goodsEvaluate,goodsEvaluateDTO);
            UserInfo userInfo = userInfoMapper.selectById(goodsEvaluate.getUserId());
            goodsEvaluateDTO.setHeadImg(userInfo.getHeadImg());
            evaluateDTOS.add(goodsEvaluateDTO);
        });
        return evaluateDTOS;
    }
}
