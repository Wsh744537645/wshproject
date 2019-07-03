package com.mpool.share.service.impl;

import com.mpool.share.exception.ShareException;
import com.mpool.share.mapper.EmailLogMapper;
import com.mpool.share.service.EmailService;
import com.mpool.share.utils.RequestResolveUtil;
import com.mpool.common.model.account.log.EmailLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Value("${spring.mail.username}")
	private String from;
	@Value("${spring.mail.host}")
	private String emailHost;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private EmailLogMapper emailLogMapper;

	/**
	 * 模板引擎
	 */
	@Autowired
	private TemplateEngine templateEngine;

	@Override
	public void sendSimple(String to, String title, String content) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(from); // 发送者
			helper.setTo(to); // 接受者
			helper.setSubject(title); // 发送标题
			Map<String, Object> va = new HashMap<>();
			va.put("captcha", content);
			va.put("title", title);
			va.put("type", "蜂鸟");
			// 发送内容
			IContext context = new Context(RequestResolveUtil.getRequestLocale(), va);
			String process = templateEngine.process("email/email.html", context);
			helper.setText(process, true);

		} catch (MessagingException e) {
			log.error("MessagingException", e);
			throw new ShareException("email.sent.fail", "邮件发送失败！");
		}
		executorService.execute(() -> {
			try {
				javaMailSender.send(message);
			} catch (Exception e) {
				log.error("send email", e);
			}
		});
		// 入库
		EmailLog entity = new EmailLog();
		entity.setCreateTime(new Date());
		entity.setEmail(from);
		entity.setDestinationEmail(to);
		entity.setEmailCode(200 + "");
		entity.setEmaliContent(content);
		entity.setDomain(emailHost);
		emailLogMapper.insert(entity);
	}

	@Override
	public void sendAlarmEmail(String to, String templateSrc, String title, IContext context) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(from); // 发送者
			helper.setTo(to); // 接受者
			helper.setSubject(title); // 发送标题
			// 发送内容
			String process = templateEngine.process(templateSrc, context);
			log.debug("------------------------------------------------------------------------------");
			log.debug("email to=> {} ", to);
			log.debug("email process=> {} ", process);
			log.debug("------------------------------------------------------------------------------");
			helper.setText(process, true);
		} catch (MessagingException e) {
			log.error("MessagingException ", e);
		}
		javaMailSender.send(message);
	}

}
