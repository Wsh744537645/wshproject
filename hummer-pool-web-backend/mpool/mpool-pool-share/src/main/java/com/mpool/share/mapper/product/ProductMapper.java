package com.mpool.share.mapper.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.share.MiningModel;
import com.mpool.common.model.share.ProductModel;
import com.mpool.share.resultVo.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 商品
 * @Author: stephen
 * @Date: 2019/5/22 16:26
 */

@Mapper
public interface ProductMapper extends BaseMapper<ProductModel> {

    /**
     * 增加算力库存
     * @param productId
     * @param accept
     */
    void addAcceptStock(@Param("productId") Integer productId, @Param("accept") Long accept);

    MiningModel getProductMiningByProductId(@Param("productId") Integer productId);

    /**
     * 获得所有当前可售的商品
     * @return
     */
    List<ProductVO> getAllActivityProduct(@Param("date") Date date);
}
