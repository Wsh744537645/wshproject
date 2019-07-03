package com.mpool.share.admin.module.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.share.ProductModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/6/24 18:02
 */

@Mapper
public interface ProductMapper extends BaseMapper<ProductModel> {
    ProductModel findOneByLock(@Param("productId") Integer productId);

    IPage<Map<String, Object>> getProductList(IPage<Map<String, Object>> page, @Param("begTime") Date begTime, @Param("endTime") Date endTime, @Param("productName") String productName);

    /**
     * 获得产品日志信息
     * @param page
     * @return
     */
    IPage<Map<String, Object>> getProductLogList(IPage<Map<String, Object>> page);
}
