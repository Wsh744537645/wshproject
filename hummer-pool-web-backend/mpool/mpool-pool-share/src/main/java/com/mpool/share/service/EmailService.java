package com.mpool.share.service;

import org.thymeleaf.context.IContext;

public interface EmailService {

	/**
	 * @param to      发送目标人
	 * @param title   发送标题
	 * @param content 内容
	 */
	void sendSimple(String to, String title, String content);

	/**
	 * 发送告警邮件
	 * 
	 * @param to
	 * @param title
	 * @param context
	 */
	void sendAlarmEmail(String to, String templateSrc, String title, IContext context);
}
