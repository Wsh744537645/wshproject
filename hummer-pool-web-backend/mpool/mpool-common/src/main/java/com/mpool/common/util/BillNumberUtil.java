package com.mpool.common.util;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

public class BillNumberUtil {
	private long lastTimestamp = 0;

	private final String machineId;

	private long sequence = 0L;

	private final int sequenceMax = 9999;

	private BillNumberUtil(String machineId) {
		this.machineId = machineId;
	}

	private long tilNextMillis(long lastTimestamp) {
		long timestamp = System.currentTimeMillis();
		while (timestamp <= lastTimestamp) {
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}

	public synchronized String nextId() {
		Date now = new Date();
		String time = DateFormatUtils.format(now, "yyMMddHHmmssSSS");
		long timestamp = now.getTime();
		if (this.lastTimestamp == timestamp) {
			this.sequence = this.sequence + 1 % this.sequenceMax;
			if (sequence == 0) {
				timestamp = this.tilNextMillis(timestamp);
			}
		} else {
			this.sequence = 0;
		}
		this.lastTimestamp = timestamp;
		StringBuilder sb = new StringBuilder(time).append(machineId).append(leftPad(sequence, 4));
		return sb.toString();
	}

	private String leftPad(long i, int n) {
		String s = String.valueOf(i);
		StringBuilder sb = new StringBuilder();
		int c = n - s.length();
		c = c < 0 ? 0 : c;
		for (int t = 0; t < c; t++) {
			sb.append("0");
		}
		return sb.append(s).toString();
	}

	private static BillNumberUtil instance = null;

	public static BillNumberUtil getInstance() {
		if (instance == null) {
			synchronized (BillNumberUtil.class) {
				if (instance == null) {
					instance = new BillNumberUtil("99");
				}
			}
		}
		return instance;
	}

	public static void main(String[] args) {
		System.out.println(getInstance().nextId());
		System.out.println("181129162310253990000".length());

	}
}
