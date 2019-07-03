package com.mpool.share.admin.module.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.Result;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import com.mpool.share.admin.model.ProductVO;
import com.mpool.share.admin.module.product.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/6/25 10:21
 */

@RestController
@RequestMapping({"/product", "/apis/product"})
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    @ApiOperation("添加商品")
    public Result addProduct(ProductVO productVO){
        productService.addProduct(productVO);
        return Result.ok();
    }

    @PostMapping("/edite")
    @ApiOperation("修改商品")
    public Result editeProduct(ProductVO productVO){
        productService.editeProduct(productVO);
        return Result.ok();
    }

    @GetMapping("/getlist")
    @ApiOperation("获得商品列表")
    public Result getProductList(PageModel pageModel, Long begTime, Long endTime, String productName){
        IPage<Map<String, Object>> page = new Page<>(pageModel);

        Date begt = null;
        Date endt = null;
        if(begTime != null && endTime != null){
            begt = new Date(begTime * 1000);
            endt = new Date(endTime * 1000);
        }
        page = productService.getProductList(page, begt, endt, productName);
        return Result.ok(page);
    }

    @PostMapping("/shutdown")
    @ApiOperation("暂停商品的收益计算")
    public Result shutdown(Integer[] productIds){
        List<Integer> list = new ArrayList<>();
        if(productIds != null) {
            for (Integer productId : productIds) {
                list.add(productId);
            }
        }
        productService.shutdown(list);
        return Result.ok();
    }

    @PostMapping("/startup")
    @ApiOperation("开启商品的收益计算")
    public Result startup(Integer[] productIds){
        List<Integer> list = new ArrayList<>();
        if(productIds != null) {
            for (Integer productId : productIds) {
                list.add(productId);
            }
        }
        productService.startup(list);
        return Result.ok();
    }
}
