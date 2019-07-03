package com.mpool.common.model.account.fpps;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("block_reward")
public class BlockReward {
	private Integer height;
	private Long rewardBlock;
	private Long rewardFees;

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Long getRewardBlock() {
		return rewardBlock;
	}

	public void setRewardBlock(Long rewardBlock) {
		this.rewardBlock = rewardBlock;
	}

	public Long getRewardFees() {
		return rewardFees;
	}

	public void setRewardFees(Long rewardFees) {
		this.rewardFees = rewardFees;
	}

}
