package com.mpool.mpoolaccountmultiplecommon.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

@ApiModel
public class WorkerGroupParamModel {
	@ApiModelProperty("组名")
	@NotEmpty
	private String groupName;
	@ApiModelProperty("序号")
	private int groupSeq;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getGroupSeq() {
		return groupSeq;
	}

	public void setGroupSeq(int groupSeq) {
		this.groupSeq = groupSeq;
	}

}
