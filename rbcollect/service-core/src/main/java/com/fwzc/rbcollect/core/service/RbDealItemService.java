package com.fwzc.rbcollect.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fwzc.rbcollect.core.pojo.entity.RbDealItem;
import com.fwzc.rbcollect.core.pojo.entity.UserInfo;
import com.fwzc.rbcollect.core.pojo.entity.dto.SortByImgDTO;
import com.fwzc.rbcollect.core.pojo.entity.dto.SortDTO;
import com.fwzc.rbcollect.core.pojo.entity.query.RbDealItemQuery;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 垃圾回收下单订单 服务类
 * </p>
 *
 * @author wzc
 * @since 2022-03-30
 */
public interface RbDealItemService extends IService<RbDealItem> {

    IPage<RbDealItem> listPage(Long userId,Page<RbDealItem> pageParm, RbDealItemQuery rbDealItemQuery);


    SortDTO getRbSort(String word) throws IOException;

    SortByImgDTO getRbSortByImg(String imgUrl) throws IOException;

    boolean saveRbDealItem(Long userId, UserInfo userInfo,RbDealItem rbDealItem);






    List<RbDealItem> listFiveItem(Long userId);

    boolean deleteRbDeal(Integer rbDealId);


    boolean sendRb(Long id);

    boolean DoneRb(Long id);


    boolean tryDeleteRbDeal(Integer rbDealId);

    boolean removeRbDealById(long id);

}
