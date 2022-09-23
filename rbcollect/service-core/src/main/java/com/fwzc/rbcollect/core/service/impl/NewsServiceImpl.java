package com.fwzc.rbcollect.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fwzc.rbcollect.core.mapper.NewsMapper;
import com.fwzc.rbcollect.core.pojo.entity.News;
import com.fwzc.rbcollect.core.service.NewsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {


    @Override
    public IPage<News> listPage(Page<News> pageParm) {
        return baseMapper.selectPage(pageParm,null);
    }
}
