package com.mpool.pool.sync.impl;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;
import com.mpool.common.model.account.Worker;
import com.mpool.common.model.constant.AccountConstant.WorkerStatus;
import com.mpool.pool.config.CanalConfig;
import com.mpool.pool.service.WorkerService;
import com.mpool.pool.sync.SyncServiceResolverV2;

/**
 * btc启动线程
 */

@Component
@Order(1)
public class SyncServiceV2Impl extends SyncServiceResolverV2 implements CommandLineRunner {

	private static final String _puid = "puid";

	private static final String _worker_id = "worker_id";

	private static final String _worker_name = "worker_name";

	private static final String _accept_15m = "accept_15m";

	private static final String _accept_5m = "accept_5m";

	private static final String _reject_15m = "reject_15m";

	private static final String _accept_1h = "accept_1h";

	private static final String _reject_1h = "reject_1h";

	private static final String _last_share_time = "last_share_time";

	private String regionId;

	private static final Logger log = LoggerFactory.getLogger(SyncServiceV2Impl.class);

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	@Qualifier("workerServiceImpl")
	public WorkerService workerService;
	@Autowired
	public CanalConfig canalConfig;

	@Override
	public List<Worker> insertHandle(List<RowData> rowDatasList) {
		return insertSQLResolve(rowDatasList);
	}

	@Override
	public List<Worker> updateHandle(List<RowData> rowDatasList) {
		List<Worker> list = updateSQLResolveWorkerInfo(rowDatasList);
		return list;
	}

	@Override
	public List<Worker> deleteHandle(List<RowData> rowDatasList) {
		return deleteSQLResolve(rowDatasList);
	}

	private List<Worker> insertSQLResolve(List<RowData> rowDatasList) {
		List<Worker> result = new ArrayList<>();
		for (RowData rowData : rowDatasList) {
			List<Column> afterColumnsList = rowData.getAfterColumnsList();
			int count = 0;
			String workerName = null;
			String workerId = null;
			String puid = null;
			for (Column column : afterColumnsList) {
				if (count < 3) {
					String columnName = column.getName();
					if (_puid.equals(columnName)) {
						puid = column.getValue();
						count++;
					} else if (_worker_id.equals(columnName)) {
						workerId = column.getValue();
						count++;
					} else if (_worker_name.equals(columnName)) {
						workerName = column.getValue();
						count++;
					}
				} else {
					break;
				}
			}
			Worker worker = new Worker();
			worker.setWorkerId(Long.parseLong(workerId));
			worker.setUserId(Long.parseLong(puid));
			worker.setWorkerName(workerName);
//			if (accept15m == null || "0".equals(accept15m)) {
			worker.setWorkerStatus(WorkerStatus.inactive.getStatus());
//			} else {
//				worker.setWorkerStatus(WorkerStatus.active.getStatus());
//			}
			worker.setHashAccept15mT(0d);
			worker.setHashAccept1hT(0d);
			worker.setHashAccept5mT(0d);
			worker.setHashReject15mT(0d);
			worker.setHashReject1hT(0d);
			worker.setRateReject15m(0d);
			worker.setRateReject1h(0d);
			result.add(worker);
		}
		return result;
	}

	private List<Worker> deleteSQLResolve(List<RowData> rowDatasList) {
		List<Worker> result = new ArrayList<>();

		for (RowData rowData : rowDatasList) {
			List<Column> afterColumnsList = rowData.getBeforeColumnsList();
			int count = 0;
			Long puid = null;
			Long workerId = null;
			String workerName = null;
			Worker worker = new Worker();

			for (Column column : afterColumnsList) {
				if (count < 3) {
					String columnName = column.getName();
					if (_puid.equals(columnName)) {
						puid = Long.parseLong(column.getValue());
						count++;
					} else if (_worker_id.equals(columnName)) {
						workerId = Long.parseLong(column.getValue());
						count++;
					} else if (_worker_name.equals(columnName)) {
						workerName = column.getValue();
						count++;
					}
				} else {
					break;
				}
			}
			worker.setUserId(puid);
			worker.setWorkerId(workerId);
			worker.setWorkerName(workerName);
			worker.setHashAccept15mT(0d);
			worker.setHashAccept1hT(0d);
			worker.setHashAccept5mT(0d);
			worker.setHashReject15mT(0d);
			worker.setHashReject1hT(0d);
			worker.setRateReject15m(0d);
			worker.setRateReject1h(0d);
			worker.setWorkerStatus(WorkerStatus.offline.getStatus());
			result.add(worker);
		}
		return result;
	}

	private List<Worker> updateSQLResolveWorkerInfo(List<RowData> rowDatasList) {
		List<Worker> workers = new ArrayList<>();
		for (RowData rowData : rowDatasList) {
			Worker worker = rowDataConvertWorker(rowData);
			if (worker != null) {
				workers.add(worker);
			}
		}
		return workers;
	}

	private Worker rowDataConvertWorker(RowData rowData) {
		List<Column> afterColumnsList = rowData.getAfterColumnsList();
		int count = 0;
		Worker worker = new Worker();
		int rowCount = 10;
		int status = 1;
		String accept_5m = null;
		String accept_1h = null;
		String accept_15m = null;
		String update_at = null;
		for (Column column : afterColumnsList) {
			if (count < rowCount) {
				String columnName = column.getName();
				String value = column.getValue();
				switch (columnName) {
				case _puid:
					worker.setUserId(Long.parseLong(value));
					count++;
					break;
				case _reject_15m:
					double hashReject15mT = getHashReject15mT(value);
					worker.setHashReject15mT(hashReject15mT);
					count++;
					break;
				case _accept_15m:
//					if (StringUtils.isEmpty(value) || "0".equals(value)) {
//						status = WorkerStatus.inactive.getStatus();
//					} else {
//						status = WorkerStatus.active.getStatus();
//					}
					accept_15m = value;
					double hashAccept15mT = getHashAccept15mT(value);
					worker.setHashAccept15mT(hashAccept15mT);
					count++;
					break;
				case _accept_1h:
					accept_1h = value;
					double hashAccept1hT = getHashAccept1hT(value);
					worker.setHashAccept1hT(hashAccept1hT);
					count++;
					break;
				case _accept_5m:
					accept_5m = value;
					double hashAccept5mT = getHashAccept5mT(value);
					worker.setHashAccept5mT(hashAccept5mT);
					count++;
					break;
				case _reject_1h:
					double hashReject1hT = getHashReject1hT(value);
					worker.setHashReject1hT(hashReject1hT);
					count++;
					break;
				case _worker_id:
					worker.setWorkerId(Long.parseLong(value));
					count++;
					break;
				case _worker_name:
					worker.setWorkerName(value);
					count++;
					break;
				case _last_share_time:
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					ParsePosition pos = new ParsePosition(0);
					Date _shartime = formatter.parse(value, pos);
					worker.setLastShareTime(_shartime);
					count++;
					break;
				case "updated_at":
					update_at = value;

					SimpleDateFormat formatterUpdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					ParsePosition pos1 = new ParsePosition(0);
					Date _updateat = formatterUpdate.parse(value, pos1);

					count++;
					break;
				default:
					break;
				}
			} else {
				break;
			}
		}

//		if(worker.getWorkerId().equals(2020030175922065730L)){
//			log.error("get cur update_at={},5m={},15m={},1h={}, data = {}", update_at, accept_5m, accept_15m, accept_1h, worker);
//		}

		//log.warn("data update time is {}", update_at);

		if (count == rowCount) {
			if (StringUtils.isEmpty(accept_15m) || "0".equals(accept_15m)) {
				Date date = new Date();
				if(date.getTime() - worker.getLastShareTime().getTime() > 24 * 60 * 60 * 1000){
					//如果15分钟算力为0且算力最后更新的时间超过了24小时，则判定为离线
					status = WorkerStatus.offline.getStatus();
				}else{
					status = WorkerStatus.inactive.getStatus();
				}
			} else {
				status = WorkerStatus.active.getStatus();
			}

			double d = worker.getHashAccept15mT() + worker.getHashReject15mT();
			if (d != 0d) {
				double doubleValue = BigDecimal.valueOf(worker.getHashReject15mT() / d)
						.setScale(3, BigDecimal.ROUND_FLOOR).doubleValue();
				worker.setRateReject15m(doubleValue);
			} else {
				worker.setRateReject15m(0d);
			}
			double e = worker.getHashAccept1hT() + worker.getHashReject1hT();
			if (e != 0d) {
				double doubleValue = BigDecimal.valueOf(worker.getHashReject1hT() / e)
						.setScale(3, BigDecimal.ROUND_FLOOR).doubleValue();
				worker.setRateReject1h(doubleValue);
			} else {
				worker.setRateReject1h(0d);
			}
			worker.setWorkerStatus(status);
		} else {
			log.warn("row error -> {}", afterColumnsList.stream().map(item -> item.getName() + ":" + item.getValue())
					.collect(Collectors.joining()));
			return null;
		}
		return worker;
	}

	@Override
	public void sync(List<Entry> entries) {
		long currentTimeMillis = System.currentTimeMillis();
		//log.debug("sync begin time {}", currentTimeMillis);
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
		//log.debug("sync resolving time {}", System.currentTimeMillis() - currentTimeMillis);
		if (!workers.isEmpty()) {
			log.warn("cur write date {}", new Date());
			workerService.replace(regionId, workers, new Date());
		}
		//log.debug("sync data row size {}", workers.size());
		//log.debug("sync end time {}", System.currentTimeMillis());
		//log.debug("sync exce time {}", System.currentTimeMillis() - currentTimeMillis);
	}

	@Override
	public void run(String... args) throws Exception {
		this.regionId = canalConfig.getRegionId();
		new Thread(() -> {
			log.debug("runing sync regionId={}", regionId);
			CanalConnector connector = null;
			long batchId = -1;
			try {
				connector = canalConfig.getCanalConnector();
				int batchSize = canalConfig.getBatchSize();
				connector.connect();
				connector.subscribe(".*\\.mining_workers");
				connector.rollback();
				while (true) {
					Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
					batchId = message.getId();
					int size = message.getEntries().size();
					if (batchId == -1 || size == 0) {
					} else {
						//log.debug("batch size {}", size);
						sync(message.getEntries());
					}
					connector.ack(batchId); // 提交确认
					// connector.rollback(batchId); // 处理失败, 回滚数据
				}
			} catch (Exception e) {
				//error("e", e);
				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper;
				try {
					helper = new MimeMessageHelper(message, true, "UTF-8");
					helper.setFrom("noreply@hummerpool.com");
					helper.setTo("862775170@qq.com"); // 接受者
					helper.setSubject("服务GG"); // 发送标题
					// 发送内容
					helper.setText("同步服务死亡了 ____" + e.getMessage());
				} catch (MessagingException e1) {
					log.error("", e1);
				}
				javaMailSender.send(message);
				connector.rollback(batchId);
				connector.disconnect();
			}
		}).start();
	}

}
