package com.mpool.admin.mapperUtils.account.mapperBase;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.InModel.InBlockchain;
import com.mpool.common.model.account.BlockchainAllModel;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: stephen
 * @Date: 2019/5/13 16:34
 */
public interface BlockchainAllBaseMapper {
    IPage<BlockchainAllModel> getBlocks(IPage<InBlockchain> iPage, @Param("date") InBlock date);
}
