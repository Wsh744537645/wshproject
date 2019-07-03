package com.mpool.share.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.share.mapper.product.MiningMapper;
import com.mpool.share.mapper.product.ProductMapper;
import com.mpool.common.model.share.MiningModel;
import com.mpool.common.model.share.ProductModel;
import com.mpool.share.resultVo.ProductVO;
import com.mpool.share.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: stephen
 * @Date: 2019/5/23 12:04
 */

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private MiningMapper miningMapper;

    @Override
    public List<ProductVO> getAllActivityProduct() {
        return productMapper.getAllActivityProduct(new Date());
    }

    @Override
    public ProductModel selectOneByProductId(Integer productId) {
        ProductModel model = new ProductModel();
        model.setProductId(productId);
        return productMapper.selectOne(new QueryWrapper<>(model));
    }

    @Override
    @Transactional
    public synchronized void update(ProductModel model) {
        ProductModel queryModel = new ProductModel();
        queryModel.setProductId(model.getProductId());
        productMapper.update(model, new QueryWrapper<>(queryModel));
    }

    @Override
    public MiningModel getProductMiningByProductId(Integer productId) {
        return productMapper.getProductMiningByProductId(productId);
    }

    @Override
    public MiningModel getMiningById(Integer mining_id) {
        MiningModel miningModel = new MiningModel();
        miningModel.setId(mining_id);
        return miningMapper.selectOne(new QueryWrapper<>(miningModel));
    }

    @Override
    @Transactional
    public synchronized void addAcceptStockBatch(Map<Integer, Long> map) {
        Set<Integer> productIds = map.keySet();
        for(Integer productId : productIds){
            Long acceptSum = map.get(productId);
            productMapper.addAcceptStock(productId, acceptSum);
        }
    }
}
