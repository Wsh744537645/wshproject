package com.mpool.accountmultiple.mapper;

import com.mpool.mpoolaccountmultiplecommon.model.PoolNodeModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/21 16:20
 */

@Mapper
public interface PoolNodeMapper {

    @Select("select * from pool_node")
    List<PoolNodeModel> getAllPoolNode();
}
