package com.mpool.admin.module.bill.service;

import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;

public interface UserPayService {

	/**
	 * 修改用户的费率
	 * 
	 * @param userId
	 * @param fppsRate
	 */
	void updateFppsRate(Long userId, Float fppsRate);

	/**
	 * @param iPage
	 * @param group
	 * @param username
	 * @return
	 */
	IPage<Map<String, Object>> getUserFppsList(IPage<Map<String, Object>> iPage, Integer group, String username,
			String recommendName);

}
