package com.fwzc.rbcollect.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fwzc.rbcollect.core.mapper.TransFlowMapper;
import com.fwzc.rbcollect.core.pojo.entity.TransFlow;
import com.fwzc.rbcollect.core.service.TransFlowService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 交易流水表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@Service
public class TransFlowServiceImpl extends ServiceImpl<TransFlowMapper, TransFlow> implements TransFlowService {

//    @Override
//    public List<TransFlow> FindFlowByDealItem(Long dealItemNo) {
//        QueryWrapper<TransFlow> wrapper = new QueryWrapper<>();
//        wrapper.eq("deal_item_no",dealItemNo);
//        List<TransFlow> transFlows = baseMapper.selectList(wrapper);
//        //关键： 1.给实体类设置自定义属性  2.使用枚举遍历
//        transFlows.forEach((transFlow)->{
//            Integer transType = transFlow.getTransType();
//            transFlow.getParam().put("FlowType", CommonEnum.getMsgByType(transType));
//        });
//        Integer count = baseMapper.selectCount(wrapper);
//        if (count!=0){
//            return transFlows;
//        }
//        return null;
//
//    }
}
