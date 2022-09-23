package com.fwzc.rbcollect.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fwzc.rbcollect.core.listener.ExcelDictDTOListener;
import com.fwzc.rbcollect.core.mapper.DictMapper;
import com.fwzc.rbcollect.core.pojo.entity.Dict;
import com.fwzc.rbcollect.core.pojo.entity.dto.ExcelDictDTO;
import com.fwzc.rbcollect.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private DictMapper dictMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = Exception.class)   //使用事务捕获所有异常回滚
    @Override
    public void importData(InputStream inputStream) {         //实体+监听器
        EasyExcel.read(inputStream, ExcelDictDTO.class, new ExcelDictDTOListener(dictMapper)).sheet().doRead();
        log.info("Excel导入成功");
    }

    @Override
    public List listDictData() {
        List<Dict> dictList = baseMapper.selectList(null);
        //从数据库得到的Dict对象要转化成转换类ExcelDict才能导出
        ArrayList<ExcelDictDTO> excelDictDTOSList = new ArrayList<>(dictList.size());
        dictList.forEach(dict->{
            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            BeanUtils.copyProperties(dict,excelDictDTO); //复制同名属性到转换类ExcelDict
            excelDictDTOSList.add(excelDictDTO);
        });
        return excelDictDTOSList;
    }

    @Override
    public List<Dict> listByParentId(Long parentId) {
        //try-catch包起来是因为防止redis出错程序会跳出
//        try {
//            //首先查询redis中是否存在数据列表
//            List<Dict> dictList =(List<Dict>) redisTemplate.opsForValue().get("rbcollect:core:dictList:" + parentId);
//            if(dictList!=null){
//                log.info("从redis获取数据");
//                //如果存在则直接返回
//                return dictList;
//            }
//        }catch (Exception e){
//            log.info("redis服务器异常", ExceptionUtils.getStackTrace(e));
//        }

        //根据当前结点id查询parent_id为该结点的id的数据
        QueryWrapper<Dict> dicQueryWrapper = new QueryWrapper<>();
        dicQueryWrapper.eq("parent_id",parentId);
        //parentId为父id  结果为可回收垃圾，不可回收。。。
        List<Dict> dictList = baseMapper.selectList(dicQueryWrapper);
        //填充hasChildren字段 element-ui要求 row 中的 hasChildren 字段来指定哪些行是包含子节点
        dictList.forEach(dict -> {
            //判断当前子节点是否还含有其子节点
            boolean hasChildren = this.hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        });
//
//        try {
//            log.info("将数据存入redis");
//            //将数据存入redis
//            redisTemplate.opsForValue().set("rbcollect:core:dictList:" + parentId,dictList,5, TimeUnit.MINUTES);
//        } catch (Exception e) {
//            log.info("redis服务器异常", ExceptionUtils.getStackTrace(e));
//        }

        return dictList;
    }

    /**
     * 判断当前id所在的节点下是否含有子节点
     */
    private boolean hasChildren(Long id){
        QueryWrapper<Dict> dicQueryWrapper = new QueryWrapper<>();
        dicQueryWrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(dicQueryWrapper);
        if (count.intValue()>0){
            return true;
        }
        return false;
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code",dictCode);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);
        return this.listByParentId(dict.getId());
    }

    @Override
    public String getNameByParentDictCodeAndValue(String dictCode, Integer value) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code",dictCode);
        Dict parentDict = baseMapper.selectOne(dictQueryWrapper);
        if (parentDict==null){
            return "";
        }
        dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id",parentDict.getId())
                .eq("value",value);
        Dict dict=baseMapper.selectOne(dictQueryWrapper);
        return dict.getName();

    }

    @Override
    public List<Dict> listByParentName(String parentName) {
        //根据当前结点name查询parent_id为该结点的id的数据
        QueryWrapper<Dict> dicQueryWrapper = null;
        dicQueryWrapper=new QueryWrapper<>();
        dicQueryWrapper.eq("name",parentName);
        Dict parentDict = baseMapper.selectOne(dicQueryWrapper);
        Long parentDictId = parentDict.getId();

        dicQueryWrapper=new QueryWrapper<>();
        dicQueryWrapper.eq("parent_id",parentDictId);

        List<Dict> dictList = baseMapper.selectList(dicQueryWrapper);
        return dictList;

    }
}
