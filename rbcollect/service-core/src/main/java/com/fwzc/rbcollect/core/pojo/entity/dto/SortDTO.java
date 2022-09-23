package com.fwzc.rbcollect.core.pojo.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName : SortVO
 * @Description : TODO
 * @Author : James
 * @Date : 2022/4/4 20:02
 * @Version 1.0
 */
@Data
public class SortDTO {
    private String code;
    private String msg;
    private List<SortDeatilDTO> newslist;
}
