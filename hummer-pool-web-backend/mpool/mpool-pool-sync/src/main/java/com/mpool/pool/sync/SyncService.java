package com.mpool.pool.sync;

import java.util.List;

import com.alibaba.otter.canal.protocol.CanalEntry.Entry;

public interface SyncService {

	void sync(List<Entry> entries);
}
