package com.mpool.account.mapper.fpps;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.fpps.BlockReward;

@Mapper
public interface BlockRewardMapper extends BaseMapper<BlockReward> {
	/**
	 * 获取 height 最大的432个
	 * 
	 * @return
	 */
	List<BlockReward> getBlockReward();
}
