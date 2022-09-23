package com.fwzc.rbcollect.core.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.fwzc.common.exception.BusinessException;
import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.core.mapper.DictMapper;
import com.fwzc.rbcollect.core.pojo.entity.Dict;
import com.fwzc.rbcollect.core.pojo.entity.dto.ExcelDictDTO;
import com.fwzc.rbcollect.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 数据字典 的Excel 导入 导出，父结点属性的处理
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@Api(tags = "数据字典管理")
@Slf4j
@RestController
@RequestMapping("/admin/core/dict")
public class AdminDictController {
    @Resource
    DictService dictService;
    @Resource
    DictMapper dictMapper;

    @ApiOperation("Excel数据的批量导入")
    @PostMapping("/import")
    public Result batchImport(
            @ApiParam(value = "Excel数据字典文件",required = true)
            @RequestParam("file") MultipartFile file){
        try {
            //导入前删除全部数据、
           dictMapper.deleteDictTemp();

            InputStream inputStream = file.getInputStream();
            dictService.importData(inputStream);
            return Result.ok().setMsg("数据字典批量导入成功");
        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR,e);
        }
    }
    @ApiOperation("Excel数据的导出")
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("数据字典", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ExcelDictDTO.class).sheet("模板").doWrite(dictService.listDictData());

    }

    @ApiOperation("根据上级id获取子节点数据列表")
    @GetMapping("/listByParentId/{parentId}")
    public Result listByParentId(
            @ApiParam(value = "上级节点id",required = true)
            @PathVariable Long parentId
    ){
        List<Dict> dictList=dictService.listByParentId(parentId);
        return Result.ok().setData("list",dictList);
    }
}

