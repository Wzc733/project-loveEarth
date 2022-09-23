package com.fwzc.rbcollect.core.utils;

/**
 * @ClassName : CountHelper
 * @Description : TODO
 * @Author : James
 * @Date : 2022/4/10 15:48
 * @Version 1.0
 */
public class CountHelper {

    public static Integer CalculationOfIntegral(String rbType,String rbWeight,Integer consumeIntegral){



            switch (rbType){
                case "20000"  : return Integer.parseInt(rbWeight)*7-consumeIntegral;
                case "30000"  : return Integer.parseInt(rbWeight)*5-consumeIntegral;
                case "40000"  : return Integer.parseInt(rbWeight)*3-consumeIntegral;
                case "50000"  : return Integer.parseInt(rbWeight)*2-consumeIntegral;
                default: return null;
            }
    }
}
