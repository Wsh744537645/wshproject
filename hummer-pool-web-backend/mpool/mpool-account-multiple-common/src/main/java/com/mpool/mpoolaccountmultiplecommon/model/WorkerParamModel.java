package com.mpool.mpoolaccountmultiplecommon.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

@ApiModel
public class WorkerParamModel {
	@ApiModelProperty("区域Id")
	private Integer regionId;
	@ApiModelProperty("矿工名称")
	@NotEmpty
	private String workerName;
	@ApiModelProperty("矿机组")
	private Long groupId;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

}
