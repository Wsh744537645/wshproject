package com.mpool.common.util.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

public class Page<T> implements IPage<T> {

	private static final Logger log = LoggerFactory.getLogger(Page.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 153904266397950934L;

	private List<T> records = new ArrayList<>();

	private long total;

	private long size = 10;

	private long current = 1;

	private String[] descs;
	private String[] ascs;

	public Page() {
		super();
	}

	public Page(long size, long current) {
		super();
		this.size = size;
		this.current = current;
	}

	public Page(PageModel model) {
		super();
		this.size = model.getSize();
		this.current = model.getCurrent();
		if (model.getAscs() != null) {
			String[] array = Stream.of(model.getAscs()).map(StringUtils::camelToUnderline).toArray(String[]::new);
			this.ascs = array;
			if (log.isDebugEnabled()) {
				log.debug("Asc camelToUnderline {} <--> {}", Arrays.toString(model.getAscs()), Arrays.toString(array));
			}
		}

		if (model.getDescs() != null) {
			String[] arr = Stream.of(model.getDescs()).map(StringUtils::camelToUnderline).toArray(String[]::new);
			this.descs = arr;
			if (log.isDebugEnabled()) {
				log.debug("Desc camelToUnderline {} <--> {}", Arrays.toString(model.getDescs()), Arrays.toString(arr));
			}
		}
	}

	public Page(IPage<?> iPage) {
		this.setCurrent(iPage.getCurrent());
		this.setSize(iPage.getSize());
		this.setTotal(iPage.getTotal());
		this.setPages(iPage.getPages());
	}

	@Override
	public List<T> getRecords() {
		return records;
	}

//	public String[] getDescs() {
//		return descs;
//	}
//
//	public void setDescs(String[] descs) {
//		this.descs = descs;
//	}
//
//	public String[] getAscs() {
//		return ascs;
//	}
//
//	public void setAscs(String[] ascs) {
//		this.ascs = ascs;
//	}

	@Override
	public IPage<T> setRecords(List<T> records) {
		this.records = records;
		return this;
	}

	@Override
	public long getTotal() {
		return total;
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public IPage<T> setSize(long size) {
		this.size = size;
		return this;
	}

	@Override
	public long getCurrent() {
		return current;
	}

	@Override
	public IPage<T> setCurrent(long current) {
		this.current = current;
		return this;
	}

	@Override
	public String[] descs() {
		return descs;
	}

	@Override
	public String[] ascs() {
		return ascs;
	}
//
//	@Override
//	public IPage<T> setTotal(Long total) {
//		this.total = total;
//		return this;
//	}

	@Override
	public IPage<T> setTotal(long total) {
		this.total = total;
		return this;
	}

}
