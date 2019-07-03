package com.mpool.common;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T> {

	void insert(T entity);

	void inserts(List<T> entitys);

	void update(T entity);

	void delete(Serializable id);

	T findById(Serializable id);
}