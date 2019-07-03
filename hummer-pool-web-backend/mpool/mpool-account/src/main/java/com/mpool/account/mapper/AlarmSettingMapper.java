package com.mpool.account.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.AlarmSetting;

@Mapper
public interface AlarmSettingMapper extends BaseMapper<AlarmSetting> {

}
