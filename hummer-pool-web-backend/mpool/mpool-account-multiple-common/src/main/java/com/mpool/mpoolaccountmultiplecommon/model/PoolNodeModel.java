package com.mpool.mpoolaccountmultiplecommon.model;

import lombok.Data;

/**
 * 矿池节点
 * @Author: stephen
 * @Date: 2019/5/21 18:14
 */

@Data
public class PoolNodeModel {
    private Integer id;
    private String region; //区域分类
    private String address;
    private Integer order; //当前分类中的顺序
    private String discrible; //描述
}
