package com.mpool.account.mapper.miner;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.account.model.miner.MinerCurrencyModel;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface MinerCurrencyMapper extends BaseMapper<MinerCurrencyModel> {
    List<MinerCurrencyModel> findByCurrencyIds(@Param("ids") int[] ids);
}
