package com.mpool.admin.module.recommend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.RecommendUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

@Mapper
public interface RecommendUserMapper extends BaseMapper<RecommendUser> {

	RecommendUser getRecommendUserByname(@Param("name") String name);
	List<RecommendUser> getRecommendUserListByname(@Param("name") String name);
	/**
	 * wgg 添加推荐人
	 * 
	 * @param modle
	 */
	void creatRecommendUser(RecommendUser modle);

	/**
	 * wgg 删除推荐人
	 */
	void deleteRecommendUser(@Param("id") Serializable id);

	RecommendUser getUserByUserId(@Param("userId") Long userId);

	void updateRecommendUser(RecommendUser recommendUser);

}
