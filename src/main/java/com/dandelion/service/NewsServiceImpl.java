package com.dandelion.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dandelion.domain.News;
import com.dandelion.domain.NewsCreateForm;
import com.dandelion.domain.User;
import com.dandelion.repository.NewsRepository;

/**
 * 新闻接口实现
 * 
 * @author jiekechoo
 *
 */
@Service
public class NewsServiceImpl implements NewsService {

	private NewsRepository newsRepository;
	private UserService userService;

	/**
	 * 注入NewsRepository
	 * 
	 * @param newsRepository
	 * @param userService
	 */
	@Autowired
	public NewsServiceImpl(NewsRepository newsRepository, UserService userService) {
		this.newsRepository = newsRepository;
		this.userService = userService;
	}

	/**
	 * 创建新闻
	 */
	public News create(NewsCreateForm form) {
		User user = userService.getCurrentUser();
		News news = new News();
		news.createNews(form.getTitle(), form.getImg(), form.getContent(), new Date(), user);
		newsRepository.save(news);
		return news;

	}

	/**
	 * 获取新闻列表
	 */
	public Page<News> getNewsList(Long startid, Pageable p) {
		Page<News> news = newsRepository.findByIdGreaterThan(startid, p);
		return news;
	}

}
