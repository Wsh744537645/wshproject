package com.mpool.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.model.CurrentWorkerStatus;
import com.mpool.common.model.account.RecommendUser;
import com.mpool.common.model.account.Worker;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RecommendUserMapper {

	List<RecommendUser> getRecommendUserList();
}
