package com.mpool.pool.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MiningWorkerMapper {

	void delete(Map<String, Object> param);

	void insert(Map<String, Object> param);

	void update(Map<String, Object> param);

	Integer checkRow(Map<String, Object> param);
}
