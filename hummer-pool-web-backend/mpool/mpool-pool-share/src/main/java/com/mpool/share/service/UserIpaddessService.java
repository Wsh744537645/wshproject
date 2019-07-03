package com.mpool.share.service;

import com.mpool.common.BaseService;
import com.mpool.common.model.account.IpRegion;

public interface UserIpaddessService extends BaseService<IpRegion> {

    void addIpAddess(String ip, Long userId);
}
