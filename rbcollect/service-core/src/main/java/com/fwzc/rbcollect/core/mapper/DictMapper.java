package com.fwzc.rbcollect.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fwzc.rbcollect.core.pojo.entity.Dict;
import com.fwzc.rbcollect.core.pojo.entity.dto.ExcelDictDTO;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@SuppressWarnings("all")
public interface DictMapper extends BaseMapper<Dict> {

    void insertBatch(List<ExcelDictDTO> list);

    //清空表
    @Update("truncate table dict")
    void deleteDictTemp();
}
