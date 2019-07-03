package com.mpool.share.controller;

import com.mpool.common.Result;
import com.mpool.share.resultVo.ProductVO;
import com.mpool.share.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/29 9:31
 */

@RestController
@RequestMapping({"/product", "apis/product"})
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/activity/all")
    public Result getAllActityProduct(){
        List<ProductVO> productVOS = productService.getAllActivityProduct();
        return Result.ok(productVOS);
    }
}
