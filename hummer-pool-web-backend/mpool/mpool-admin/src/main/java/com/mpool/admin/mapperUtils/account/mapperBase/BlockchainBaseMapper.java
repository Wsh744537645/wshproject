package com.mpool.admin.mapperUtils.account.mapperBase;

import com.mpool.common.model.account.Blockchain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/13 17:08
 */
public interface BlockchainBaseMapper {
    List<Blockchain> gethashRate(@Param("days") List<String> days);
}
