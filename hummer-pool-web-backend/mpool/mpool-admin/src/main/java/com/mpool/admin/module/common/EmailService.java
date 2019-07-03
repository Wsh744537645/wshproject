package com.mpool.admin.module.common;

import org.thymeleaf.context.IContext;

public interface EmailService {

	void sendAlarmEmail(String userEmail, String templateEmail, String string, IContext context);

}
