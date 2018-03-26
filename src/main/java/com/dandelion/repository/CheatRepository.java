package com.dandelion.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.dandelion.domain.Cheat;
import com.dandelion.domain.User;

/**
 * 新闻表Repository定义
 * 
 * @author qing
 *
 */
@RestResource(exported = false)

public interface CheatRepository extends PagingAndSortingRepository<Cheat, Long>  {

	
	Page<Cheat> findByIdGreaterThan(Long startid, Pageable p);
	

	Cheat findByCheatwechat(String cheatwechat);
	
}
