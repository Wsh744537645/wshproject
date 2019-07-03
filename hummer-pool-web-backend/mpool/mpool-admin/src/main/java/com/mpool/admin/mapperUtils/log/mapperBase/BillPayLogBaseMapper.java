package com.mpool.admin.mapperUtils.log.mapperBase;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/14 14:28
 */
public interface BillPayLogBaseMapper {
    IPage<Map<String, Object>> getBillPayLog(IPage<Map<String, Object>> ipage);
}
