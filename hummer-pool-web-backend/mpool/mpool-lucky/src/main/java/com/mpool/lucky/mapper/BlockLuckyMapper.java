package com.mpool.lucky.mapper;

import com.mpool.lucky.model.BlockLuckyModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/8 11:41
 */

@Mapper
public interface BlockLuckyMapper {
    @Select("select * from btc_pool_lucky_all where pool_id = 0 ORDER BY creat_at DESC LIMIT 100")
    List<BlockLuckyModel> getPoolLuckyAll();

    @Select("select * from btc_pool_lucky_month where pool_id = 0 ORDER BY creat_at DESC LIMIT 100")
    List<BlockLuckyModel> getPoolLuckyMonth();

    @Select("select * from btc_pool_lucky_week where pool_id = 0 ORDER BY creat_at DESC LIMIT #{limit}")
    List<BlockLuckyModel> getPoolLuckyWeek(@Param("limit")Integer limit);
}
