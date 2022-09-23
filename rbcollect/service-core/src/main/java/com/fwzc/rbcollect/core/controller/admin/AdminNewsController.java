package com.fwzc.rbcollect.core.controller.admin;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fwzc.common.exception.Assert;
import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.result.Result;
import com.fwzc.rbcollect.core.pojo.entity.News;
import com.fwzc.rbcollect.core.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 新闻板块 增删改查
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@Api(tags = "新闻模块管理")
@Slf4j
@RestController
@RequestMapping("/admin/core/news")
public class AdminNewsController {

    @Resource
    NewsService newsService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String CACHE_NEWS_KEY = "cache:news:";

    @ApiOperation("获取新闻分页列表")
    @GetMapping("/list/{page}/{limit}")
    public Result listPage(
            @ApiParam(value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数",required = true)
            @PathVariable Long limit){
        //Page对象包含当前页码,每页记录数
        Page<News> pageParm = new Page<>(page,limit);



        //从redis查询缓存
        String news = stringRedisTemplate.opsForValue().get(CACHE_NEWS_KEY+page);
        IPage<News> pageModel = JSONObject.parseObject(news, Page.class);

        //命中，直接返回
        if (pageModel!=null) {
            return Result.ok().setData("pageModel",pageModel);
        }
        //没命中，查询
        pageModel=newsService.listPage(pageParm);
//        //将查询出来的新闻对象中文字内容置空
//        List<News> records = pageModel.getRecords();
//        records.forEach(newsItem -> {
//            newsItem.setContent("");
//        });
//        pageModel.setRecords(records);

        //不存在，返回错误
        if (pageModel==null){
            return Result.error().setMsg("新闻不存在");
        }
        //存在,写入redis,返回
        stringRedisTemplate.opsForValue().set(CACHE_NEWS_KEY+page, JSON.toJSON(pageModel).toString(),30L, TimeUnit.MINUTES);
        return Result.ok().setData("pageModel",pageModel);
    }






    @ApiOperation("新增新闻")
    @PostMapping("/save")
    public Result save(
            @ApiParam(value = "新闻对象",required = true)
            @RequestBody News news){

        Assert.notNull(news.getContent(), ResponseEnum.NEW_CONTENT_NULL_ERROE);
        try {
            boolean result = newsService.save(news);
            if(result) {
                return Result.ok().setMsg("保存成功");
            }else {
                return Result.error().setMsg("保存失败");
            }
        }catch (Exception e){
            return Result.error().setMsg("保存失败");
        }

    }

    @ApiOperation("根据id查询新闻")
    @GetMapping("/get/{id}")
    public Result getById(
            @ApiParam(value = "数据id",required = true,example = "1")
            @PathVariable Long id){
        News news = newsService.getById(id);

        if(news!=null){
            return Result.ok().setData("record",news).setMsg("查询成功");
        }else {
            return Result.error().setMsg("查询失败");
        }
    }
    @ApiOperation("更新新闻")
    @PostMapping("/update")
    public Result updateById(
            @ApiParam(value = "新闻对象",required = true)
            @RequestBody News news){
        boolean result = newsService.updateById(news);
        if(result){
            return Result.ok().setMsg("更新成功");
        }else {
            return Result.error().setMsg("更新失败");
        }
    }

    @ApiOperation(value = "根据id删除新闻", notes = "逻辑删除")  //notes 进一步说明
    @DeleteMapping("/remove/{id}")
    public Result removeById(
            @ApiParam(value = "新闻id", required = true, example = "1")   //example默认值
            @PathVariable long id){
        boolean result = newsService.removeById(id);
        if(result){
            return Result.ok().setMsg("删除成功");
        }else {
            return Result.error().setMsg("删除失败");
        }
    }
}

