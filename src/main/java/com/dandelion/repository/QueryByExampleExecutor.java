package com.dandelion.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface QueryByExampleExecutor<T> {
	<S extends T> S findOne(Example<S> example); //根据“实例”查找一个对象。
	<S extends T> Iterable<S> findAll(Example<S> example); //根据“实例”查找一批对象
	<S extends T> Iterable<S> findAll(Example<S> example, Sort sort); //根据“实例”查找一批对象，且排序
	<S extends T> Page<S> findAll(Example<S> example, Pageable pageable); //根据“实例”查找一批对象，且排序和分页
	<S extends T> long count(Example<S> example); //根据“实例”查找，返回符合条件的对象个数
	<S extends T> boolean exists(Example<S> example); //根据“实例”判断是否有符合条件的对象
}
