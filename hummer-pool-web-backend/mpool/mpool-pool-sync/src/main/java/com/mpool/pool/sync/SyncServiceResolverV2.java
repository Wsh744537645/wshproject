package com.mpool.pool.sync;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.mpool.common.model.account.Worker;
import com.mpool.common.model.pool.utils.MathShare;

public abstract class SyncServiceResolverV2 implements SyncService {

	private static final Logger log = LoggerFactory.getLogger(SyncServiceResolverV2.class);

	private static final int M5 = 60 * 5;
	private static final int M15 = 60 * 15;

	public Double getHashAccept5mT(String accept5m) {
		if (StringUtils.isEmpty(accept5m)) {
			return 0d;
		}
		return MathShare.mathShareDouble(Long.parseLong(accept5m), M5);
	}

	public Double getHashAccept15mT(String accept15m) {
		if (StringUtils.isEmpty(accept15m)) {
			return 0d;
		}
		return MathShare.mathShareDouble(Long.parseLong(accept15m), M15);
	}

	public Double getHashReject15mT(String reject15m) {
		if (StringUtils.isEmpty(reject15m)) {
			return 0d;
		}
		return MathShare.mathShareDouble(Long.parseLong(reject15m), M15);
	}

	public Double getHashAccept1hT(String accept1h) {
		if (StringUtils.isEmpty(accept1h)) {
			return 0d;
		}
		return MathShare.MathShareHourDouble(Long.parseLong(accept1h));
	}

	public Double getHashReject1hT(String reject1h) {
		if (StringUtils.isEmpty(reject1h)) {
			return 0d;
		}
		return MathShare.MathShareHourDouble(Long.parseLong(reject1h));
	}

	@Override
	public void sync(List<Entry> entries) {
		long currentTimeMillis = System.currentTimeMillis();
		log.debug("sync begin time {}", currentTimeMillis);
		List<Worker> workers = new ArrayList<Worker>();
		for (Entry entry : entries) {
			if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN
					|| entry.getEntryType() == EntryType.TRANSACTIONEND) {
				continue;
			}
			RowChange rowChage = null;
			try {
				rowChage = RowChange.parseFrom(entry.getStoreValue());
			} catch (Exception e) {
				throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
						e);
			}

			EventType eventType = rowChage.getEventType();
			List<RowData> rowDatasList = rowChage.getRowDatasList();
			if (eventType == EventType.INSERT) {
				List<Worker> insertHandle = insertHandle(rowDatasList);
				if (!insertHandle.isEmpty()) {
					workers.addAll(insertHandle);
				}
			} else if (eventType == EventType.UPDATE) {
				List<Worker> updateHandle = updateHandle(rowDatasList);
				if (!updateHandle.isEmpty()) {
					workers.addAll(updateHandle);
				}
			} else if (eventType == EventType.DELETE) {
				List<Worker> deleteHandle = deleteHandle(rowDatasList);
				if (!deleteHandle.isEmpty()) {
					workers.addAll(deleteHandle);
				}
			} else {
				// No processing required
			}
		}
		log.debug("sync end time {}", System.currentTimeMillis());
		log.debug("sync exce time {}", System.currentTimeMillis() - currentTimeMillis);
	}

	/**
	 * Insert SQL 处理方法
	 * 
	 * @param rowDatasList
	 */
	public abstract List<Worker> insertHandle(List<RowData> rowDatasList);

	/**
	 * Update SQL 处理方法
	 * 
	 * @param rowDatasList
	 */
	public abstract List<Worker> updateHandle(List<RowData> rowDatasList);

	/**
	 * Delete SQL 处理方法
	 * 
	 * @param rowDatasList
	 */
	public abstract List<Worker> deleteHandle(List<RowData> rowDatasList);
}
