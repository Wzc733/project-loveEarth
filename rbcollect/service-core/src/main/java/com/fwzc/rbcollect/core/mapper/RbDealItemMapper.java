package com.fwzc.rbcollect.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fwzc.rbcollect.core.pojo.entity.RbDealItem;
import com.fwzc.rbcollect.core.pojo.entity.dto.RbDealChartsDTO;

import java.util.List;

/**
 * <p>
 * 垃圾回收下单订单 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2022-03-30
 */
public interface RbDealItemMapper extends BaseMapper<RbDealItem> {


    List<RbDealChartsDTO> getRbDealForCharts(Long userId);
}
