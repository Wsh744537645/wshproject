package com.mpool.accountmultiple.mapper.fpps;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.fpps.BlockReward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlockRewardMapper extends BaseMapper<BlockReward> {
	/**
	 * 获取 height 最大的432个
	 * 
	 * @return
	 */
	List<BlockReward> getBlockReward();
}
