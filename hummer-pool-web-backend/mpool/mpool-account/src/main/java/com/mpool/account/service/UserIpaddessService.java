package com.mpool.account.service;

import com.mpool.common.model.account.IpRegion;

public interface UserIpaddessService extends BaseService<IpRegion>{

    void addIpAddess(String ip,Long userId);
}
