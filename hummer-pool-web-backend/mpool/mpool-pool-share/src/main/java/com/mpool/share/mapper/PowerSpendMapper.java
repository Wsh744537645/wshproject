package com.mpool.share.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.share.model.PowerSpendModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/6/5 12:06
 */

@Mapper
public interface PowerSpendMapper extends BaseMapper<PowerSpendModel> {
    void inserts(@Param("list") List<PowerSpendModel> list);
}
