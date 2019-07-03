package com.mpool.share.admin.model;

import com.mpool.common.model.share.ProductModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/6/25 10:26
 */

@Data
public class ProductVO {
    private Integer productId;

    @NotEmpty
    private String productName;

    /**
     * 可购买最小算力
     */
    @NotEmpty
    private Long minAccept;

    /**
     * 算力每TH/s的美元价格
     */
    @NotEmpty
    private Double acceptFee;

    /**
     * 电费:每1算力的美元价格
     */
    @NotEmpty
    private Double powerFee;

    /**
     * 周期
     */
    @NotEmpty
    private Integer period;

    /**
     * 币种id
     */
    private Integer currencyId = 1;

    /**
     * 算力库存
     */
    @NotEmpty
    private Long stock;

    /**
     * 电费价格 美元/度
     */
    private Float powerPrice;

    /**
     * 生效时间
     */
    @NotEmpty
    private Long workTime;

    /**
     * 上架时间
     */
    @NotEmpty
    private Long shelfTime;

    /**
     * 下架时间
     */
    @NotEmpty
    private Long obtainedTime;

    public ProductModel getProduct(){
        ProductModel model = new ProductModel();
        model.setProductId(this.getProductId());
        model.setProductName(this.productName);
        model.setMinAccept(this.minAccept);
        model.setAcceptFee(this.acceptFee);
        model.setPowerFee(this.powerFee);
        model.setPeriod(this.period);
        model.setCurrencyId(this.currencyId);
        model.setCurrencyName("BTC");
        model.setStock(this.stock);
        model.setPowerPrice(this.powerPrice);
        if(this.workTime != null) {
            model.setWorkTime(new Date(this.workTime * 1000));
        }
        if(this.shelfTime != null) {
            model.setShelfTime(new Date(this.shelfTime * 1000));
        }
        if(this.obtainedTime != null) {
            model.setObtainedTime(new Date(this.obtainedTime * 1000));
        }
        model.setCreateTime(new Date());
        model.setUpdateTime(new Date());

        return model;
    }
}
