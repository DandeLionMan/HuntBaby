package com.dandelion.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dandelion.domain.News;
import com.dandelion.domain.NewsCreateForm;

/**
 * 新闻接口
 * 
 * @author jiekechoo
 *
 */
public interface NewsService {

	News create(NewsCreateForm form);

	Page<News> getNewsList(Long startid, Pageable p);

}
