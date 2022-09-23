package com.fwzc.rbcollect.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fwzc.rbcollect.core.mapper.DictMapper;
import com.fwzc.rbcollect.core.pojo.entity.dto.ExcelDictDTO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 王梓超
 * @Auther:wzc
 * @Data:2021/11/26 - 11 - 26 - 11:47
 */
@Slf4j
@NoArgsConstructor
public class ExcelDictDTOListener extends AnalysisEventListener<ExcelDictDTO> {
    //每隔5条记录批量存储一次数据
    private static final int BATCH_COUNT=5;

    private DictMapper dictMapper;

    public ExcelDictDTOListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }


    List<ExcelDictDTO> list=new ArrayList<>();

    @Override
    public void invoke(ExcelDictDTO data, AnalysisContext analysisContext) {
        log.info("解析到一条记录:{}",data);
        list.add(data);
        if(list.size()>=BATCH_COUNT){
            //最后不足5条记录的，在doAfterAllAnalysed收尾方法存进去
            saveData();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        log.info("数据解析完成");
    }

    private void saveData(){
        log.info("{}条数据被存储到数据库",list.size());
        //save list方法
        dictMapper.insertBatch(list);
        log.info("{}条数据存储到数据库成功",list.size());
    }
}
