package com.mpool.accountmultiple.notify.impl;

import com.mpool.accountmultiple.mapper.AlarmNotifyUserMapper;
import com.mpool.accountmultiple.mapper.UserMapper;
import com.mpool.common.properties.MultipleProperties;
import com.mpool.mpoolaccountmultiplecommon.model.NotifyModel;
import com.mpool.accountmultiple.notify.NotifyUserShareService;
import com.mpool.accountmultiple.service.AlarmService;
import com.mpool.accountmultiple.service.EmailService;
import com.mpool.accountmultiple.service.SmsService;
import com.mpool.common.model.account.AlarmNotifyUser;
import com.mpool.common.model.account.AlarmSetting;
import com.mpool.common.model.account.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class NotifyUserShareServiceImpl implements NotifyUserShareService {

	private static final Logger log = LoggerFactory.getLogger(NotifyUserShareServiceImpl.class);

	private final static String template_email = "email/notifyUserShare.html";

	private final static String template_phone = "SMS_152544365";

	private final static String hashKey = "hash:UserShare:";

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
	@Autowired
	private MultipleProperties multipleProperties;

	private final String prefix = "Notify:UserShare:";
	private Map<Long, Integer> threshold = new HashMap<Long, Integer>();

	@Override
	public void notifyUser(List<AlarmSetting> alarmSettings) {

		if (alarmSettings == null || alarmSettings.isEmpty()) {
			log.debug("-------------------> No users need to be alerted <-----------------------");
			return;
		}
		Map<Long, Double> userShare = alarmService.getUserShare();
		for (final AlarmSetting alarmSetting : alarmSettings) {

			final Long userId = alarmSetting.getUserId();

			List<AlarmNotifyUser> alarmNotifyUsers = alarmNotifyUserMapper.getNotifyUsers(userId);
			if (alarmNotifyUsers.size() == 0) {
				// 不需要发送通知
				log.debug("notify user number {}", alarmNotifyUsers.size());
				continue;
			}

			if (redisTemplate.opsForValue().get(multipleProperties.getName() + prefix + userId) != null) {
				// 不需要发送通知
				log.debug("userId => {}  continue", userId);
				continue;
			}
			Integer value = threshold.get(userId);
			if (value == null) {
				value = 1;
			} else {
				value += 1;
			}
			int flag = 0;
			final Double thresholdShare = alarmSetting.getUserShare();
			final Double ushare = userShare.get(userId);
			if (ushare != null) {
				if (thresholdShare != null && (!thresholdShare.equals(0d))) {
					// 判断算力
					if (ushare < thresholdShare) {
						flag++;
					} else {
						// 可以不要
						threshold.put(userId, 1);
					}
				}
			}
			if (flag != 0) {
				if (value > thresholdValue) {
					// 通知
					value = 0;

					final User user = userMapper.selectById(userId);
					NotifyModel notifyModel = new NotifyModel();
					notifyModel.setName(user.getUsername());
					notifyModel.setValue(ushare.toString());
					int hashCode = notifyModel.hashCode();
					String key = hashKey + userId;

					boolean flg = checkSendCount(hashCode + "", key);

					if (flg) {
						sendNotify(alarmNotifyUsers, user, notifyModel);
					}

					int timeout = alarmSetting.getCycle();
					redisTemplate.opsForValue().set(multipleProperties.getName() + prefix + userId, "", timeout, TimeUnit.SECONDS);
				}
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
