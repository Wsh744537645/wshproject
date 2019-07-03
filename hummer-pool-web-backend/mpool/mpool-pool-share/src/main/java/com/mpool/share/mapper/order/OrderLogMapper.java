package com.mpool.share.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.share.OrderLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/29 15:43
 */

@Mapper
public interface OrderLogMapper extends BaseMapper<OrderLog> {
    void inserts(@Param("logs") List<OrderLog> logs);
}
