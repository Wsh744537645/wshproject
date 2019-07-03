package com.mpool.pool.mapper;

import com.mpool.common.model.account.Worker;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ZecWorkerMapper {
	Integer checkRow(Map<String, Object> map);

	void insert(Map<String, Object> map);

	void inserts(@Param("param") List<Map<String, Object>> param);

	void replace(@Param("regionId") String regionId, @Param("param") List<Map<String, String>> param);

	void updateWorkerStatus(@Param("workerStatus") Integer workerStatus, @Param("regionId") String regionId,
                            @Param("puids") List<String> puids, @Param("workerIds") List<String> workerIds);

	void replaceByWorker(@Param("list") List<Worker> list, @Param("regionId") String regionId,
                         @Param("date") Date date);

	void replaceAll(@Param("regionId") String regionId, @Param("workerList") List<Worker> workers,
                    @Param("date") Date date);
}
