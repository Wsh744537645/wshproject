package com.mpool.admin.module.pool.service;

import java.util.List;

public interface StatusUsersDayService {
	List<Long> getShareByPuidAndDay(List<Long> puid, Long day);
	
}
