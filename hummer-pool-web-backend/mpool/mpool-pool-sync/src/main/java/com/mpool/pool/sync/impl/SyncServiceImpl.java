package com.mpool.pool.sync.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;
import com.mpool.common.model.account.Worker;
import com.mpool.common.model.constant.AccountConstant.WorkerStatus;
import com.mpool.pool.config.CanalConfig;
import com.mpool.pool.service.WorkerService;
import com.mpool.pool.sync.SyncServiceResolver;


public class SyncServiceImpl extends SyncServiceResolver implements CommandLineRunner {

	private static final String map_key_puids = "puids";

	private static final String map_key_workerIds = "workerIds";

	private static final String _puid = "puid";

	private static final String _worker_id = "worker_id";

	private static final String _worker_name = "worker_name";

	private static final String _accept_15m = "accept_15m";

	private static final String _accept_5m = "accept_5m";

	private static final String _reject_15m = "reject_15m";

	private static final String _accept_1h = "accept_1h";

	private static final String _reject_1h = "reject_1h";

	private String regionId;

	private static final Logger log = LoggerFactory.getLogger(SyncServiceImpl.class);

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	public WorkerService workerService;
	@Autowired
	public CanalConfig canalConfig;

	@Override
	public void insertHandle(List<RowData> rowDatasList) {
		List<Map<String, String>> insertSQLResolve = insertSQLResolve(rowDatasList);
		if (insertSQLResolve.size() > 0) {
			workerService.replace(regionId, insertSQLResolve);
		}
	}

	@Override
	public void updateHandle(List<RowData> rowDatasList) {
//		Map<Integer, Map<String, List<String>>> updateSQLResolve = updateSQLResolve(rowDatasList);
		List<Worker> list = updateSQLResolveWorkerInfo(rowDatasList);
		if (list.size() > 0) {
			workerService.replace(list, regionId, new Date());
		}
//		for (Entry<Integer, Map<String, List<String>>> e : updateSQLResolve.entrySet()) {
//			Integer workerStatus = e.getKey();
//			Map<String, List<String>> value = e.getValue();
//			List<String> puids = value.get(map_key_puids);
//			List<String> workerIds = value.get(map_key_workerIds);
//			workerService.updateWorkerStatus(workerStatus, regionId, puids, workerIds);
//		}
	}

	@Override
	public void deleteHandle(List<RowData> rowDatasList) {
		Map<String, List<String>> deleteSQLResolve = deleteSQLResolve(rowDatasList);
		List<String> puids = deleteSQLResolve.get(map_key_puids);
		List<String> workerIds = deleteSQLResolve.get(map_key_workerIds);
		if (deleteSQLResolve.size() > 0) {
			workerService.updateWorkerStatus(WorkerStatus.offline.getStatus(), regionId, puids, workerIds);
		}

	}

	private List<Map<String, String>> insertSQLResolve(List<RowData> rowDatasList) {
		List<Map<String, String>> result = new ArrayList<>();
		for (RowData rowData : rowDatasList) {
			List<Column> afterColumnsList = rowData.getAfterColumnsList();
			int count = 0;
			String workerName = null;
			String workerId = null;
			String puid = null;
			String accept15m = null;
			for (Column column : afterColumnsList) {
				if (count < 4) {
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
					} else if (_accept_15m.equals(columnName)) {
						accept15m = column.getValue();
						count++;
					}
				} else {
					break;
				}
			}
			/*
			 * (#{tiem.worker_id}, #{tiem.puid}, #{tiem.region_id}, #{tiem.worker_name})
			 */
			// workerid 不能等于0
//			if (!"0".equals(workerId)) {
			Map<String, String> hashMap = new HashMap<>();
			hashMap.put("worker_id", workerId);
			hashMap.put("puid", puid);
			hashMap.put("worker_name", workerName);
			if (accept15m == null || "0".equals(accept15m)) {
				hashMap.put("worker_status", WorkerStatus.inactive.getStatus().toString());
			} else {
				hashMap.put("worker_status", WorkerStatus.active.getStatus().toString());
			}
			result.add(hashMap);
//			}
		}
		return result;
	}

	private Map<String, List<String>> deleteSQLResolve(List<RowData> rowDatasList) {
		List<String> puids = new ArrayList<>();
		List<String> workerIds = new ArrayList<>();

		for (RowData rowData : rowDatasList) {
			List<Column> afterColumnsList = rowData.getBeforeColumnsList();
			int count = 0;
			for (Column column : afterColumnsList) {
				if (count < 2) {
					String columnName = column.getName();
					if (_puid.equals(columnName)) {
						puids.add(column.getValue());
						count++;
					} else if (_worker_id.equals(columnName)) {
						workerIds.add(column.getValue());
						count++;
					}
				} else {
					break;
				}
			}
		}
		Map<String, List<String>> result = new HashMap<>();
		if (puids.size() != 0 && workerIds.size() != 0) {
			result.put(map_key_puids, puids);
			result.put(map_key_workerIds, workerIds);
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
		int status = 1;
		for (Column column : afterColumnsList) {
			if (count < 8) {
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
					if (StringUtils.isEmpty(value) || "0".equals(value)) {
						status = WorkerStatus.inactive.getStatus();
					} else {
						status = WorkerStatus.active.getStatus();
					}
					double hashAccept15mT = getHashAccept15mT(value);
					worker.setHashAccept15mT(hashAccept15mT);
					count++;
					break;
				case _accept_1h:
					double hashAccept1hT = getHashAccept1hT(value);
					worker.setHashAccept1hT(hashAccept1hT);
					count++;
					break;
				case _accept_5m:
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
				default:
					break;
				}
			} else {
				break;
			}
		}
		if (count == 8) {
			double d = worker.getHashAccept15mT() + worker.getHashReject15mT();
			if (d != 0d) {
				worker.setRateReject15m(worker.getHashReject15mT() / d);
			} else {
				worker.setRateReject15m(0d);
			}
			double e = worker.getHashAccept1hT() + worker.getHashReject1hT();
			if (e != 0d) {
				worker.setRateReject1h(worker.getHashReject1hT() / e);
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

//	private Map<Integer, Map<String, List<String>>> updateSQLResolve(List<RowData> rowDatasList) {
//		Map<Integer, Map<String, List<String>>> param = new HashMap<>();
//
//		List<String> puidActive = new ArrayList<>();
//		List<String> workerIdActive = new ArrayList<>();
//		List<String> puidInactive = new ArrayList<>();
//		List<String> workerIdInactive = new ArrayList<>();
//
//		for (RowData rowData : rowDatasList) {
//			String puid = null;
//			String workerId = null;
//			String accept_15m = null;
//			List<Column> afterColumnsList = rowData.getAfterColumnsList();
//			int count = 0;
//			for (Column column : afterColumnsList) {
//				if (count < 3) {
//					String columnName = column.getName();
//					if (_puid.equals(columnName)) {
//						puid = column.getValue();
//						count++;
//					} else if (_worker_id.equals(columnName)) {
//						workerId = column.getValue();
//						count++;
//					} else if (_accept_15m.equals(columnName)) {
//						accept_15m = column.getValue();
//						count++;
//					}
//				} else {
//					break;
//				}
//			}
//			if (!"0".equals(workerId)) {
//				if (accept_15m == null || "0".equals(accept_15m)) {
//					// 非活跃
//					puidInactive.add(puid);
//					workerIdInactive.add(workerId);
//				} else {
//					// 活跃
//					puidActive.add(puid);
//					workerIdActive.add(workerId);
//				}
//			}
//		}
//		if (puidInactive.size() != 0) {
//			HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
//			hashMap.put(map_key_puids, puidInactive);
//			hashMap.put(map_key_workerIds, workerIdInactive);
//			param.put(1, hashMap);
//		}
//		if (puidActive.size() != 0) {
//			HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
//			hashMap.put(map_key_puids, puidActive);
//			hashMap.put(map_key_workerIds, workerIdActive);
//			param.put(0, hashMap);
//		}
//		return param;
//	}

	@Override
	public void run(String... args) throws Exception {
		this.regionId = canalConfig.getRegionId();
		new Thread(() -> {
			log.debug("runing sync regionId={}", regionId);
			CanalConnector connector = null;
			long batchId = -1;
			try {
				connector = canalConfig.getCanalConnector();
				int batchSize = 1000;
				connector.connect();
				connector.subscribe(".*\\.mining_workers");
				connector.rollback();
				while (true) {
					Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
					batchId = message.getId();
					int size = message.getEntries().size();
					if (batchId == -1 || size == 0) {
					} else {
						log.debug("batch size {}", size);
						sync(message.getEntries());
					}
					connector.ack(batchId); // 提交确认
					// connector.rollback(batchId); // 处理失败, 回滚数据
				}
			} catch (Exception e) {
				log.error("e", e);
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
