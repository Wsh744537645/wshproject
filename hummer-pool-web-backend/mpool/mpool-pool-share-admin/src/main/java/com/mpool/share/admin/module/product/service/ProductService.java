package com.mpool.share.admin.module.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.admin.model.ProductVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/6/24 18:03
 */
public interface ProductService {

    void addProduct(ProductVO productVO);

    void editeProduct(ProductVO productVO);

    IPage<Map<String, Object>> getProductList(IPage<Map<String, Object>> page, Date begTime, Date endTime, String productName);

    /**
     * 获得产品日志信息
     * @param page
     * @return
     */
    IPage<Map<String, Object>> getProductLogList(IPage<Map<String, Object>> page);

    /**
     * 暂停某个商品的收益计算
     * @param productIds
     */
    void shutdown(List<Integer> productIds);

    /**
     * 恢复商品的收益计算机制
     * @param productIds
     */
    void startup(List<Integer> productIds);
}
