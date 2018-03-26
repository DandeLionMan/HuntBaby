package com.dandelion.repository;

import org.springframework.data.repository.CrudRepository;

import com.dandelion.domain.Core;

public interface CoreRepository extends CrudRepository<Core, Long> {
	
	Core findById(long id);

}
