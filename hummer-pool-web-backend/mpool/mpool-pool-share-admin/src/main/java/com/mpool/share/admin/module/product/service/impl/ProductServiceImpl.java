package com.mpool.share.admin.module.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.log.LogUserOperation;
import com.mpool.common.model.product.OrderModel;
import com.mpool.common.model.share.OrderLog;
import com.mpool.common.model.share.ProductModel;
import com.mpool.common.util.DateUtil;
import com.mpool.share.admin.exception.AdminException;
import com.mpool.share.admin.exception.ExceptionCode;
import com.mpool.share.admin.model.LogProduct;
import com.mpool.common.model.share.ProductStateModel;
import com.mpool.share.admin.model.ProductVO;
import com.mpool.share.admin.module.dashboard.mapper.OrderMapper;
import com.mpool.share.admin.module.log.mapper.LogAdminOperationMpper;
import com.mpool.share.admin.module.log.mapper.LogProductMapper;
import com.mpool.share.admin.module.log.mapper.OrderLogMapper;
import com.mpool.share.admin.module.product.mapper.ProductMapper;
import com.mpool.share.admin.module.product.mapper.ProductStateMapper;
import com.mpool.share.admin.module.product.service.ProductService;
import com.mpool.share.admin.module.system.service.SysUserService;
import com.mpool.share.admin.utils.AdminSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: stephen
 * @Date: 2019/6/24 18:03
 */

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductStateMapper productStateMapper;
    @Autowired
    private LogProductMapper logProductMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private LogAdminOperationMpper logAdminOperationMpper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderLogMapper orderLogMapper;

    @Override
    @Transactional
    public void addProduct(ProductVO productVO) {
        ProductModel model = productVO.getProduct();
        productMapper.insert(model);

        ProductStateModel productStateModel = new ProductStateModel();
        productStateModel.setProductId(model.getProductId());
        productStateModel.setUpdateTime(DateUtil.addDay(new Date(), -2));
        productStateModel.setCreateTime(new Date());
        productStateMapper.insert(productStateModel);

        LogProduct logProduct = new LogProduct();
        logProduct.setProductId(model.getProductId());
        logProduct.setType("创建");
        logProduct.setDecrible("content：" + productVO.toString());
        logProduct.setOperateBy(sysUserService.getCurrentSysuser().getUsername());
        logProduct.setCreateTime(new Date());
        logProductMapper.insert(logProduct);
    }

    private String getProductLog(ProductModel pre, ProductModel vo){
        String result = "";
        if(vo.getStock() != null){
            result = result + " stock=" + pre.getStock() + "->" + vo.getStock();
        }
        if(vo.getAcceptFee() != null){
            result = result + " acceptFee=" + pre.getAcceptFee() + "->" + vo.getAcceptFee();
        }
        if(vo.getPowerFee() != null){
            result = result + " powerFee=" + pre.getPowerFee() + "->" + vo.getPowerFee();
        }
        if(vo.getPeriod() != null){
            result = result + " period=" + pre.getPeriod() + "->" + vo.getPeriod();
        }
        if(vo.getWorkTime() != null){
            result = result + " workTime=" + pre.getWorkTime() + "->" + vo.getWorkTime();
        }
        if(vo.getShelfTime() != null){
            result = result + " shelfTime=" + pre.getShelfTime() + "->" + vo.getShelfTime();
        }
        if(vo.getObtainedTime() != null){
            result = result + " obtainedTime=" + pre.getObtainedTime() + "->" + vo.getObtainedTime();
        }
        if(vo.getPowerPrice() != null){
            result = result + " powerPrice=" + pre.getPowerPrice() + "->" + vo.getPowerPrice();
        }
        return result;
    }

    @Override
    @Transactional
    public void editeProduct(ProductVO productVO) {
        ProductModel model = productMapper.findOneByLock(productVO.getProductId());
        if(model == null){
            throw new AdminException(ExceptionCode.product_not_exists);
        }

        ProductModel modelQuery = productVO.getProduct();
        modelQuery.setCreateTime(null);
        productMapper.updateById(modelQuery);

        LogProduct logProduct = new LogProduct();
        logProduct.setProductId(modelQuery.getProductId());
        logProduct.setType("修改");
        logProduct.setDecrible(getProductLog(model, modelQuery));
        logProduct.setOperateBy(sysUserService.getCurrentSysuser().getUsername());
        logProduct.setCreateTime(new Date());
        logProductMapper.insert(logProduct);
    }

    @Override
    public IPage<Map<String, Object>> getProductList(IPage<Map<String, Object>> page, Date begTime, Date endTime, String productName) {
        return productMapper.getProductList(page, begTime, endTime, productName);
    }

    @Override
    public IPage<Map<String, Object>> getProductLogList(IPage<Map<String, Object>> page) {
        return productMapper.getProductLogList(page);
    }

    @Override
    @Transactional
    public void shutdown(List<Integer> productIds) {
        productStateMapper.updateProductState(productIds, productIds.size(), 0, new Date());

        String log = "暂停商品:";
        if(productIds.isEmpty()){
            log = log + "all";
        }else{
            log = log + productIds.toString();
        }
        LogUserOperation logUserOperation = new LogUserOperation();
        logUserOperation.setUserId(AdminSecurityUtils.getUser().getUserId());
        logUserOperation.setUserType("admin");
        logUserOperation.setLogType(5);
        logUserOperation.setContent(log);
        logUserOperation.setCreatedTime(new Date());
        logAdminOperationMpper.insert(logUserOperation);
    }

    @Override
    @Transactional
    public void startup(List<Integer> productIds) {
        Date date = new Date();
        List<ProductStateModel> productStateModelList = productStateMapper.getProductStateList(productIds, productIds.size());
        Map<Integer, Integer> dayMap = new HashMap<>();
        List<Integer> productIdList = new ArrayList<>();
        productStateModelList.forEach(model -> {
            Integer day = DateUtil.differentDays(date, model.getUpdateTime()) + 1;
            dayMap.put(model.getProductId(), day);
            productIdList.add(model.getProductId());
        });


        List<Map<String, Object>> orders = orderMapper.getOrderIdsListByProductId(productIdList);
        List<OrderModel> orderModelList = new ArrayList<>();
        List<OrderLog> orderLogList = new ArrayList<>();
        orders.forEach(orderInfo -> {
            String oId = orderInfo.get("orderId").toString();
            Integer pId = Integer.parseInt(orderInfo.get("productId").toString());
            Long expireTime = Long.parseLong(orderInfo.get("expireTime").toString());
            OrderModel orderModel = new OrderModel();
            orderModel.setOrderId(oId);
            orderModel.setExpireTime(DateUtil.addDay(new Date(expireTime * 1000), dayMap.get(pId)));
            orderModelList.add(orderModel);

            OrderLog orderLog = new OrderLog();
            orderLog.setOrderId(oId);
            orderLog.setCoin(0D);
            orderLog.setCreateTime(new Date());
            orderLog.setDiscrible("商品暂停后被重启，订单的到期时间被延长" + dayMap.get(pId) + "天");
            orderLogList.add(orderLog);
        });
        orderMapper.updateOrderExpiredTime(orderModelList);

        productStateMapper.updateProductState(productIds, productIds.size(), 1, new Date());

        orderLogMapper.inserts(orderLogList);

        String log = "开启商品:";
        if(productIds.isEmpty()){
            log = log + "all";
        }else{
            log = log + productIds.toString();
        }
        LogUserOperation logUserOperation = new LogUserOperation();
        logUserOperation.setUserId(AdminSecurityUtils.getUser().getUserId());
        logUserOperation.setUserType("admin");
        logUserOperation.setLogType(5);
        logUserOperation.setContent(log);
        logUserOperation.setCreatedTime(new Date());
        logAdminOperationMpper.insert(logUserOperation);
    }
}
