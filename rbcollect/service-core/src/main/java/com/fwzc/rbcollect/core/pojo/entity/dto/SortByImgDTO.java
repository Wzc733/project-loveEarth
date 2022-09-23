package com.fwzc.rbcollect.core.pojo.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName : SortByImgDTO
 * @Description : TODO
 * @Author : James
 * @Date : 2022/4/4 23:41
 * @Version 1.0
 */
@Data
public class SortByImgDTO {
    private String code;
    private String msg;
    private List<SortByImgDeatilDTO> newslist;
}
