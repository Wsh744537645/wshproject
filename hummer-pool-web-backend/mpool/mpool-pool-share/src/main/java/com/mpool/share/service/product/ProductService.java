package com.mpool.share.service.product;

import com.mpool.common.model.share.MiningModel;
import com.mpool.common.model.share.ProductModel;
import com.mpool.share.resultVo.ProductVO;

import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/23 12:03
 */
public interface ProductService {
    /**
     * 获得所有当前可售的商品信息
     * @return
     */
    List<ProductVO> getAllActivityProduct();

    ProductModel selectOneByProductId(Integer productId);

    void update(ProductModel model);

    MiningModel getProductMiningByProductId(Integer productId);
    MiningModel getMiningById(Integer mining_id);

    /**
     * 批量增加库存
     * @param map
     */
    void addAcceptStockBatch(Map<Integer, Long> map);
}
