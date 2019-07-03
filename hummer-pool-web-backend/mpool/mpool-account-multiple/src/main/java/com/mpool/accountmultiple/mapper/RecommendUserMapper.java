package com.mpool.accountmultiple.mapper;

import com.mpool.common.model.account.RecommendUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecommendUserMapper {

	List<RecommendUser> getRecommendUserList(@Param("currencyId") Integer currencyId);
}
