package com.mpool.share.mapper.recommend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.share.model.RecommendBillItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/6/4 11:56
 */

@Mapper
public interface RecommendBillItemMapper extends BaseMapper<RecommendBillItem> {
    void inserts(@Param("list") List<RecommendBillItem> list);
}
