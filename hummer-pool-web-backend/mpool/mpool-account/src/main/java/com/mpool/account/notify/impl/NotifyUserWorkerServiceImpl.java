package com.mpool.account.notify.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import com.mpool.account.mapper.AlarmNotifyUserMapper;
import com.mpool.account.mapper.UserMapper;
import com.mpool.account.model.NotifyModel;
import com.mpool.account.notify.NotifyUserWorkerService;
import com.mpool.account.service.AlarmService;
import com.mpool.account.service.EmailService;
import com.mpool.account.service.SmsService;
import com.mpool.common.model.account.AlarmNotifyUser;
import com.mpool.common.model.account.AlarmSetting;
import com.mpool.common.model.account.User;

@Service
public class NotifyUserWorkerServiceImpl implements NotifyUserWorkerService {

	private static final Logger log = LoggerFactory.getLogger(NotifyUserWorkerServiceImpl.class);

	private final static String template_email = "email/notifyUserWorker.html";

	private final static String template_phone = "SMS_152549262";

	private Integer thresholdValue = 3;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private AlarmService alarmService;
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private EmailService emailService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private AlarmNotifyUserMapper alarmNotifyUserMapper;

	private final static String prefix = "Notify:UserWorker:";

	private final static String hashKey = "hash:UserWorker:";

	private Map<Long, Integer> threshold = new HashMap<Long, Integer>();

	@Override
	public void notifyUser(List<AlarmSetting> alarmSettings) {

		if (alarmSettings == null || alarmSettings.isEmpty()) {
			log.debug("-------------------> No users need to be alerted <-----------------------");
			return;
		}
		Map<Long, Integer> userWorkerActive = alarmService.getUserWorkerActive();

		for (final AlarmSetting alarmSetting : alarmSettings) {

			// redisTemplate.opsForHash().
			final Long userId = alarmSetting.getUserId();
			List<AlarmNotifyUser> alarmNotifyUsers = alarmNotifyUserMapper.getNotifyUsers(userId);
			if (alarmNotifyUsers.size() == 0) {
				// 不需要发送通知
				log.debug("notify user number {}", alarmNotifyUsers.size());
				continue;
			}

			if (redisTemplate.opsForValue().get(prefix + userId) != null) {
				// 通知周期已经发送过邮件
				log.debug("userId => {} continue", userId);
				continue;
			}
			// 获得当前检测次数
			Integer value = threshold.get(userId);
			if (value == null) {
				value = 1;
			} else {
				value += 1;
			}
			int flag = 0;
			final Integer thresholdWorkerActive = alarmSetting.getWorkerActive();
			final Integer workerActive = userWorkerActive.get(userId);
			if (workerActive != null) {
				// = null = 0 不检测
				if (thresholdWorkerActive != null && !thresholdWorkerActive.equals(0)) {
					// 判断矿工活跃
					if (workerActive < thresholdWorkerActive) {
						flag++;
					} else {
						// 没有达到预期值 设置1
						threshold.put(userId, 1);
					}
				}
			}
			if (flag != 0) {
				// 连续3次检测到超过预期值才发送
				if (value > thresholdValue) {
					// 通知
					value = 0;
					final User user = userMapper.selectById(userId);
					NotifyModel notifyModel = new NotifyModel();
					notifyModel.setName(user.getUsername());
					notifyModel.setValue(workerActive.toString());
					// 判断是否发送了3次
					int hashCode = notifyModel.hashCode();
					String key = hashKey + userId;

					boolean flg = checkSendCount(hashCode + "", key);

					if (flg) {
						sendNotify(alarmNotifyUsers, user, notifyModel);
					}

					int timeout = alarmSetting.getCycle();
					redisTemplate.opsForValue().set(prefix + userId, "", timeout, TimeUnit.SECONDS);
				}
				// 阈值
				threshold.put(userId, value);
			} else {
				threshold.put(userId, 1);
			}
		}
	}

	/**
	 * 发送通知
	 * 
	 * @param notifyUsers 被通知人
	 * @param user        user
	 * @param notifyModel 通知的消息
	 */
	private void sendNotify(List<AlarmNotifyUser> notifyUsers, final User user, NotifyModel notifyModel) {
		log.debug("notifyModel => {}", notifyModel);
		for (AlarmNotifyUser alarmNotifyUser : notifyUsers) {
			// 如果有邮件就发送
			if (!StringUtils.isEmpty(alarmNotifyUser.getEmail())) {
				executorService.execute(() -> {
					Map<String, Object> va = new HashMap<>();
					va.put("name", notifyModel.getName());
					va.put("value", notifyModel.getValue());
					// 发送内容
					IContext context;
					try {
						context = new Context(new Locale(user.getLangType()), va);
					} catch (Exception e) {
						context = new Context(Locale.getDefault(), va);
					}
					emailService.sendAlarmEmail(alarmNotifyUser.getEmail(), template_email, "告警", context);
				});
			}
			// 如果有手机就发送
			if (!StringUtils.isEmpty(alarmNotifyUser.getPhone())) {
				executorService.execute(() -> {
					smsService.sendAlarmSms(alarmNotifyUser.getPhone(), template_phone, notifyModel);
				});
			}
		}
	}

	/**
	 * 判断 redis 当前key 在 redis 中有没有存在值不存在 说明是第一次发送 存在就需要判断 当前发送对象的hash值对应的value
	 * 是不是为null如果null 需要清除当前key下面所有hashkey 从新设置新的hashkey 发送了3次就不发送了
	 * 
	 * @param hashCode
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean checkSendCount(String hashCode, String key) {
		boolean flg = true;
		Map entries = redisTemplate.opsForHash().entries(key);
		log.debug("entries is empty hashCode ={} , key ={}", hashCode, key);
		if (entries == null) {
			redisTemplate.opsForHash().put(key, hashCode, "1");
		} else {
			log.debug("entries values in {}", entries);
			Object object = entries.get(hashCode);

			if (object == null) {
				for (Object hash : entries.keySet()) {
					redisTemplate.opsForHash().delete(key, hash);
				}
				redisTemplate.opsForHash().put(key, hashCode, "1");
			} else {
				int a = Integer.parseInt(object.toString());
				if (a > 3) {
					flg = false;
				} else {
					a++;
					redisTemplate.opsForHash().put(key, hashCode, "" + a);
				}
			}
		}
		return flg;
	}
}
