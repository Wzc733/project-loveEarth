package com.fwzc.rbcollect.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fwzc.rbcollect.core.pojo.entity.Goods;

import java.util.List;

/**
 * <p>
 * 商品记录表 服务类
 * </p>
 *
 * @author wzc
 * @since 2022-03-07
 */
public interface GoodsService extends IService<Goods> {

    boolean updateByIdwithAdd(Goods goods);

    List<Goods> selectGoods(String goodsName);

}
