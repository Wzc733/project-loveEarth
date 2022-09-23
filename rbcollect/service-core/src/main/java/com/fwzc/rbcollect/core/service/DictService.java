package com.fwzc.rbcollect.core.service;

import com.fwzc.rbcollect.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
public interface DictService extends IService<Dict> {

    void importData(InputStream inputStream);

    List listDictData();

    List<Dict> listByParentId(Long parentId);

    List<Dict> findByDictCode(String dictCode);
    String getNameByParentDictCodeAndValue(String dictCode, Integer value);

    List<Dict> listByParentName(String parentName);
}
