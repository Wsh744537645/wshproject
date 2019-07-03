package com.mpool.common.util.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PageModel {
	@ApiModelProperty("页大小")
	private long size = 10;
	@ApiModelProperty("当前页")
	private long current = 1;

	@ApiModelProperty("降序字段")
	private String[] descs;
	@ApiModelProperty("升序字段")
	private String[] ascs;

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getCurrent() {
		return current;
	}

	public void setCurrent(long current) {
		this.current = current;
	}

	public String[] getDescs() {
		return descs;
	}

	public void setDescs(String[] descs) {
		this.descs = descs;
	}

	public String[] getAscs() {
		return ascs;
	}

	public void setAscs(String[] ascs) {
		this.ascs = ascs;
	}

}
