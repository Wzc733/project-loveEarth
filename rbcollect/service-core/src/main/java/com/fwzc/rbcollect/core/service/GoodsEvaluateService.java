package com.fwzc.rbcollect.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fwzc.rbcollect.core.pojo.entity.GoodsEvaluate;
import com.fwzc.rbcollect.core.pojo.entity.dto.GoodsEvaluateDTO;

import java.util.List;

/**
 * <p>
 * 商品评价表 服务类
 * </p>
 *
 * @author wzc
 * @since 2022-04-15
 */
public interface GoodsEvaluateService extends IService<GoodsEvaluate> {




    boolean savegoodsEvaluate(GoodsEvaluate goodsEvaluate, Long userId);

    List<GoodsEvaluateDTO> listFiftyEvaluate(String goodsId);
}
