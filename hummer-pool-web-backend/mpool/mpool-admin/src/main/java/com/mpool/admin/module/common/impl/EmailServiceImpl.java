package com.mpool.admin.module.common.impl;

import java.util.concurrent.ExecutorService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import com.mpool.admin.module.common.EmailService;

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

	/**
	 * 模板引擎
	 */
	@Autowired
	private TemplateEngine templateEngine;

	@Override
	public void sendAlarmEmail(String to, String templateSrc, String title, IContext context) {
		executorService.execute(() -> {
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
		});

	}

}
