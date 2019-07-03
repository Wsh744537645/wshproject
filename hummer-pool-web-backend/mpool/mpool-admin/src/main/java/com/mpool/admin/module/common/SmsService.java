package com.mpool.admin.module.common;

public interface SmsService {

	void sendAlarmSms(String userPhone, String templatePhone, String param);

}
