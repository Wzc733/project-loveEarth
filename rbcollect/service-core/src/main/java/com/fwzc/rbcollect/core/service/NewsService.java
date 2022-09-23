package com.fwzc.rbcollect.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fwzc.rbcollect.core.pojo.entity.News;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 *
 * @author wzc
 * @since 2022-03-06
 */
public interface NewsService extends IService<News> {

    IPage<News> listPage(Page<News> pageParm);
}
