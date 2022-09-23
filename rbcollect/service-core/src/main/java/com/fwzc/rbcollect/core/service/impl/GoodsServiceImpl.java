package com.fwzc.rbcollect.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fwzc.rbcollect.core.mapper.GoodsMapper;
import com.fwzc.rbcollect.core.pojo.entity.Goods;
import com.fwzc.rbcollect.core.service.GoodsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品记录表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2022-03-07
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Override
    public boolean updateByIdwithAdd(Goods goods) {
        String volume = goods.getVolume();
        Long id = goods.getId();
        Goods goods1 = baseMapper.selectById(id);
        int volumeWithAdd = Integer.parseInt(volume)+Integer.parseInt(goods1.getVolume());
        goods.setVolume(volumeWithAdd+"");
        if (baseMapper.updateById(goods)!=0){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public List<Goods> selectGoods(String goodsName) {
        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        goodsQueryWrapper.like(StringUtils.isNotBlank(goodsName),"name",goodsName);
        List<Goods> goods = baseMapper.selectList(goodsQueryWrapper);
        return goods;

    }
}
