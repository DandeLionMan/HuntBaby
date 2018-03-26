package com.dandelion.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.dandelion.domain.AccountBusinessCreateInfo;
import com.dandelion.domain.Cheat;
import com.dandelion.domain.CheatItem;
import com.dandelion.domain.User;

/**
 * 新闻表Repository定义
 * 
 * @author qing
 *
 */
@RestResource(exported = false)

public interface CheatListRepository extends PagingAndSortingRepository<CheatItem, Long>  {

	List<CheatItem> findByCheatwechat(String cheatwechat);
	
	Collection<CheatItem> findByStatus(int status);
}
