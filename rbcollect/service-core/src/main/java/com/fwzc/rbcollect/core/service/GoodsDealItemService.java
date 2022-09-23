package com.fwzc.rbcollect.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fwzc.rbcollect.core.pojo.entity.Goods;
import com.fwzc.rbcollect.core.pojo.entity.GoodsDealItem;
import com.fwzc.rbcollect.core.pojo.entity.UserInfo;
import com.fwzc.rbcollect.core.pojo.entity.query.GoodsDealItemQuery;

import java.util.List;

/**
 * <p>
 * 商品购买下单订单 服务类
 * </p>
 *
 * @author wzc
 * @since 2022-03-30
 */
public interface GoodsDealItemService extends IService<GoodsDealItem> {

    IPage<GoodsDealItem> listPage(Long userId,Page<GoodsDealItem> pageParm, GoodsDealItemQuery goodsDealItem);



    boolean saveGoodsDealItem(Long userId, Goods goods, UserInfo userInfo,Integer margin, Integer num);

    List<GoodsDealItem> listFiveItem(Long userId);

    boolean deleteGoodsDeal(Integer goodsDealId);

    boolean sendGoods(Long id);

    boolean tryDeleteGoodsDeal(Integer goodsDealId);

    boolean removeGoodsById(long id);

    boolean DoneGoods(Long id);

}
