package com.mpool.account.mapper.miner;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.account.model.miner.MinerModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

@Mapper
public interface MinerMapper extends BaseMapper<MinerModel> {
    List<Map<String, Object>> findMinerByCurrencyId(@Param("id") int id);
}
