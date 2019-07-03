package com.mpool.account.mapper.block;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.block.BlockDifficulty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlockDifficultyMapper extends BaseMapper<BlockDifficulty> {
    void insertList(@Param("list") List<BlockDifficulty> list);

    Map<String, Object> getNewBlock();

    void updateOneByDiff(@Param("one") BlockDifficulty one);

    @Select("select * from block_difficulty order by time desc")
    List<BlockDifficulty> getAll();
}
