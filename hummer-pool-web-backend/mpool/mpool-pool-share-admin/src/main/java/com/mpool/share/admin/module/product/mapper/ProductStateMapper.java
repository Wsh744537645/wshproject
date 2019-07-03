package com.mpool.share.admin.module.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.share.ProductStateModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/7/1 14:43
 */

@Mapper
public interface ProductStateMapper extends BaseMapper<ProductStateModel> {
    List<ProductStateModel> getProductStateList(@Param("productIds") List<Integer> productIds, @Param("size") Integer size);

    void updateProductState(@Param("productIds") List<Integer> productIds, @Param("size") Integer size, @Param("enabled") Integer enabled, @Param("date") Date date);
}
