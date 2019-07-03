package com.mpool.account.mapper.block;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.block.Blocks;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlocksMapper extends BaseMapper<Blocks> {
    void insertBlocks(@Param("list") List<Blocks> list);

    int getCount();
}
