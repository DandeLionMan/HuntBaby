package com.dandelion.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.dandelion.domain.User;

/**
 * 用户User CrudRepository定义
 * 
 * @author jiekechoo
 *
 */
@RestResource(exported = false) // 禁止暴露REST

public interface UserRepository extends CrudRepository<User, Long> {

	Collection<User> findAll();

	User findByUsername(String username);

	Page<User> findAll(Pageable p);

	Page<User> findByUsernameContaining(String searchPhrase, Pageable p);
	
	User findById(Long id);

}
