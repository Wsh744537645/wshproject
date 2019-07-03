package com.mpool.admin.module.recommend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.InModel.InBlockchain;
import com.mpool.admin.InModel.OutUserFppsRatio;
import com.mpool.common.model.account.RecommendUser;
import com.mpool.common.model.account.UserFeeRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public interface RecommendUserService  {
    /**
     * wgg
     * 创建推荐人
     * @param model
     */
    void creatRecommendUser(RecommendUser model);

    /**
     * 删除分币人
     * @param id
     */
    void deleteRecommendUser(Serializable id);

    /**
     * wgg
     * 历史分币记录
     * @param page
     * @param date 开始时间 结束时间
     * @return
     */
    Map<String,Object> getHistoryApartCoin(IPage<UserFeeRecord> page, InBlock date );


    /**
     * 获取会员类型 费率 挖矿模式
     * @param page
     * @param in
     * @return
     */
    IPage<OutUserFppsRatio> getFppsRateAndUserTypeList(IPage<OutUserFppsRatio> page,  OutUserFppsRatio in);
    /**
     * 导出历史会员类型 费率 挖矿模式
     * @param in
     * @return
     */
    List<List<Object>> exportHistoryFppsRateAndUserTypeList(IPage<OutUserFppsRatio> page, OutUserFppsRatio in);
    /**
     * 导出历史分币记录
     * @param date
     * @return
     */
    List<List<Object>> exportHistoryApartCoinList(IPage<UserFeeRecord> page,InBlock date);

	/**
	 * @param day 获得今日分币数
	 * @return
	 */
	Long getCurrentCoin(Date day);
}
