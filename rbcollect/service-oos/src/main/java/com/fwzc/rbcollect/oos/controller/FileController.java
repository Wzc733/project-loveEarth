package com.fwzc.rbcollect.oos.controller;


import com.fwzc.common.exception.BusinessException;
import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.oos.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Auther:wzc
 * @Data:2021/11/29 - 11 - 29 - 16:40
 */
@Api(tags = "阿里云文件管理")
@RestController
@RequestMapping("/api/oss/file")
public class FileController {
    @Resource
    private FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result upload(
            @ApiParam(value = "文件",required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(value = "模块",required = true)
            @RequestParam("module") String module
            ) {
        try {
            InputStream inputStream = file.getInputStream();
            //获得文件原始名 给service取文件后缀
            String originalFilename = file.getOriginalFilename();
            String url = fileService.upload(inputStream, module, originalFilename);
            return Result.ok().setMsg("文件上传成功").setData("url",url);
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR);
        }

    }

    @ApiOperation("删除oss文件")
    @DeleteMapping("/remove")
    public Result remove(
            @ApiParam(value = "要删除的文件",required = true)
            @RequestParam("url") String url){
        fileService.removeFile(url);
        return Result.ok().setMsg("删除成功");

    }
    @ApiOperation("删除oss图片")
    @DeleteMapping("/removeImg")
    public Result removeImg(
            @ApiParam(value = "要删除的文件",required = true)
            @RequestParam("url") String url){
        fileService.removeImg(url);
        return Result.ok().setMsg("删除成功");

    }
    @ApiOperation("请求oss文件地址")
    @GetMapping("/get")
    public Result get(
            @ApiParam(value = "要请求的文件的前缀(icon/rb)", required = true)
            @RequestParam String prefix){
        List<String> urlList=fileService.getFileUrlList(prefix);
        return Result.ok().setData("urlList",urlList);
    }

    @ApiOperation("图片上传")
    @PostMapping("/uploadImg")
    public Result uploadImg(
            @ApiParam(value = "文件",required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(value = "模块",required = true)
            @RequestParam("module") String module
    ) {
        try {
            InputStream inputStream = file.getInputStream();
            //获得文件原始名 给service取文件后缀
            String originalFilename = file.getOriginalFilename();
            String url = fileService.uploadImg(inputStream, module, originalFilename);
            return Result.ok().setMsg("图片上传成功").setData("imgUrl",url);
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR);
        }
    }

}
