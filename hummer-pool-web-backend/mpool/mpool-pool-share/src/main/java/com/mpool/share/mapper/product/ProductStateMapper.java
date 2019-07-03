package com.mpool.share.mapper.product;

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
    List<ProductStateModel> getProductStateList();
}
