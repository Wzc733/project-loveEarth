package com.fwzc.rbcollect.core.utils;

import com.fwzc.rbcollect.core.pojo.entity.IntegralGrade;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName : ConsumeHelper
 * @Description : TODO
 * @Author : James
 * @Date : 2022/4/10 16:37
 * @Version 1.0
 */
public class ConsumeHelper {
    public static Integer ConsumeHOfIntegral(Integer weight, List<IntegralGrade> integralGrades){
        AtomicReference<Integer> consume= new AtomicReference<>(0);
        integralGrades.forEach(integralGrade -> {
            Integer weightStart = integralGrade.getWeightStart();
            Integer weightEnd = integralGrade.getWeightEnd();
            if(weightStart<=weight&&weight<=weightEnd ){
                consume.set(integralGrade.getIntegralAmount());
            }
        });
            return consume.get();
    }
}
