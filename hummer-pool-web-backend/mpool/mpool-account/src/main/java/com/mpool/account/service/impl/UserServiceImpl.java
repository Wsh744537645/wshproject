package com.mpool.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mpool.account.config.shiro.token.UsernamePasswordTelphoneToken;
import com.mpool.account.constant.Constant.WorkerStatus;
import com.mpool.account.exception.AccountException;
import com.mpool.account.exception.ExceptionCode;
import com.mpool.account.mapper.UserIpAddessMapper;
import com.mpool.account.mapper.UserMapper;
import com.mpool.account.mapper.WorkerMapper;
import com.mpool.account.mapper.log.UserRegLogMapper;
import com.mpool.account.mapper.pool.PoolMapper;
import com.mpool.account.model.CurrentWorkerStatus;
import com.mpool.account.model.UserInfo;
import com.mpool.account.model.subaccount.SubAccountList;
import com.mpool.account.service.*;
import com.mpool.account.service.bill.UserPayService;
import com.mpool.account.service.log.UserOperationService;
import com.mpool.account.utils.AccountSecurityUtils;
import com.mpool.common.CoinValidationUtil;
import com.mpool.common.cache.service.EmailCodeCacheService;
import com.mpool.common.cache.service.PhoneCodeCahceService;
import com.mpool.common.model.account.Currency;
import com.mpool.common.model.account.IpRegion;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.UserRegion;
import com.mpool.common.model.account.bill.UserPay;
import com.mpool.common.model.account.log.UserRegLog;
import com.mpool.common.model.log.LogUserOperation;
import com.mpool.common.model.pool.MiningWorkers;
import com.mpool.common.properties.MultipleProperties;
import com.mpool.common.util.*;
import com.mpool.rpc.RpcInstance;
import com.mpool.rpc.account.producer.btc.MultipleShare;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.*;

import static com.mpool.account.constant.Constant.CODE_REG_PREFIX;
import static com.mpool.account.constant.Constant.CODE_REST_PASSWORD_PREFIX;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	/**
	 * 用户表操作
	 */
	@Autowired
	private UserMapper usersMapper;
	/**
	 * 发送邮件服务
	 */
	@Autowired
	private EmailService mailService;
	/**
	 * 邮件缓存服务
	 */
	@Autowired
	private EmailCodeCacheService emailCodeCacheService;
	/**
	 * 用户角色服务
	 */
	@Autowired
	private UserRoleService userRoleService;
	/**
	 * 用户区域服务
	 */
	@Autowired
	private UserRegionSerice userRegionSerice;
	/**
	 * 发送短信服务
	 */
	@Autowired
	private SmsService smsService;
	/**
	 * 短信验证码缓存服务
	 */
	@Autowired
	private PhoneCodeCahceService phoneCodeCahceService;

	@Autowired
	private UserPayService btcUserPayService;
	@Autowired
	private UserRegLogMapper userRegLogMapper;
	@Autowired
	private PoolMapper poolMapper;
	@Autowired
	private WorkerMapper workerMapper;
	@Autowired
	private UserOperationService userOperationService;

	/**
	 * 每次登录确实是否为新ip，新ip需要获取地理位置保存数据库
	 */
	@Autowired
	private UserIpAddessMapper userIpAddessMapper;

	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private RpcInstance rpcInstance;
	@Autowired
	private MultipleProperties multipleProperties;
	@Autowired
	private WorkerService workerService;

	@Override
	public User findByUsername(String username) {
		return usersMapper.findByUsername(username);
	}

	@Override
	public int reg(User users) {
		Date date = new Date();
		String password = users.getPassword();
		users.setCreateTime(date);
		users.setUserSex("未知");
		users.setNickName(users.getUsername());
		users.setPassword(EncryUtil.encrypt(password));
		users.setUserType("master");
		//新用户默认为会员0
		users.setUserGroup(0);
		int insert = usersMapper.insert(users);
		userRoleService.saveUserRole(users.getUserId(), 1);
		UserPay userPay = new UserPay();
		userPay.setPuid(users.getUserId());
		btcUserPayService.insert(userPay);
		UserRegion userRegion = new UserRegion();
		userRegion.setUserId(users.getUserId());
		userRegion.setRegionId(1);
		userRegionSerice.seva(userRegion);
		//记录日志
		UserRegLog entity = new UserRegLog();
		entity.setCreateBy(0L);
		entity.setCreateTime(date);
		entity.setDesc("注册");
		entity.setRegIp(AccountSecurityUtils.getHost());
		entity.setUserId(users.getUserId());
		userRegLogMapper.insert(entity);
		LogUserOperation model = new LogUserOperation();
		model.setUserId(users.getUserId());
		model.setUserType("account");
		model.setLogType(1);
		model.setContent("注册主账户");
		model.setCreatedTime(date);
		userOperationService.insert(model);//插入log_user_operation表
		return insert;
	}

	@Override
	public void login(String username, String password, String telphoneCode) {
		Session session = AccountSecurityUtils.getSubject().getSession();
		UsernamePasswordTelphoneToken token = new UsernamePasswordTelphoneToken(username, password, telphoneCode);
		Subject subject = AccountSecurityUtils.getSubject();
		//login 会 直接到UsernamePasswordRealm离认证方法设置的来验证
		subject.login(token);
		User users = this.findByUsername(username);
		String ip = session.getHost();
		users.setLastIp(ip);
		users.setLastTime(new Date());
		usersMapper.updateById(users);
		IpRegion ipRegion = userIpAddessMapper.getIpAddess(ip);
		if (ipRegion == null){
			IpRegion model = new IpRegion();
			model.setUserId(users.getUserId());
			model.setCreatedTime(new Date());
			model.setIp(ip);
			String strIp = getCityInfoByIp(ip);
			if (strIp == null){
				model.setRegion("未知区域");
			}else{
				model.setRegion(strIp);
			}
			userIpAddessMapper.insert(model);
		}

	}

	private static String getCityInfoByIp(String ip){
//		ip = "183.14.132.72";
		if (ip.equals("0:0:0:0:0:0:0:1")){
			return null;
		}
		RestTemplate restTemplate = new RestTemplate();
		try {
			ResponseEntity<String> forEntity = restTemplate
					.getForEntity("http://freeapi.ipip.net/{ip}", String.class,ip);
			String fromJson =forEntity.getBody();

			String countries = fromJson.substring(2,4);
			String province = fromJson.substring(7,9);
			String city = fromJson.substring(12,14);
			String name = countries+"|"+province+"|"+city;
			return name;
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}

	@Override
	public String sendCodeByEmail(String mail) {
		User users = usersMapper.findBymail(mail);
		if (users != null) {
			throw new AccountException(ExceptionCode.email_exists);
		}
		String code = UUIDUtil.generateNumber(6);
		String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
		String key = CODE_REG_PREFIX + emailCodeCacheService.builderKey(sessionId, code);
		emailCodeCacheService.putCode(key, code);
		try {
			mailService.sendSimple(mail, "注册验证码", code);
		} catch (Exception e) {
			log.debug("", e);
		}

		return code;
	}

	@Override
	public String resetPasswordCode(String mail) {
		User users = usersMapper.findBymail(mail);
		if (users == null) {
			throw new AccountException(ExceptionCode.email_not_find);
		}
		String code = UUIDUtil.generateNumber(6);
		String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
		String key = CODE_REST_PASSWORD_PREFIX + emailCodeCacheService.builderKey(sessionId, code);
		emailCodeCacheService.putCode(key, code);
		mailService.sendSimple(mail, "修改密码验证码", code);
		return code;
	}

	@Override
	public void resetPassword(User user) {
		User u = null;
		if (!StringUtils.isEmpty(user.getUserEmail())) {
			u = usersMapper.findBymail(user.getUserEmail());
			if (u == null) {
				throw new AccountException(ExceptionCode.email_not_find);
			}
		}
		if (!StringUtils.isEmpty(user.getUserPhone())) {
			u = usersMapper.findByPhone(user.getUserPhone());
			if (u == null) {
				throw new AccountException(ExceptionCode.phone_not_exists);
			}
		}
		if (u == null) {
			throw new AccountException(ExceptionCode.user_not_exists);
		}
		User userr = new User();
		userr.setPassword(EncryUtil.encrypt(user.getPassword()));
		userr.setUserId(u.getUserId());
		usersMapper.updateById(userr);
	}

	@Override
	public void checkExistByMail(String mail) {
		if (usersMapper.findBymail(mail) != null) {
			throw new AccountException(ExceptionCode.email_exists);
		}
	}

	@Override
	public void checkExistByUsername(String username) {
		if (usersMapper.findByUsername(username) != null) {
			throw new AccountException(ExceptionCode.username_exists);
		}
	}

	@Override
	public void checkExistByPhone(String phone) {
		if (usersMapper.findByPhone(phone) != null) {
			throw new AccountException(ExceptionCode.phone_exists);
		}
	}

	@Override
	@Transactional
	public void createSubAccount(User users, UserPay userPay, UserRegion userRegion) {
		String password = users.getPassword();
		users.setCreateTime(new Date());
		users.setUserSex("未知");
		users.setNickName(users.getUsername());
		users.setPassword(EncryUtil.encrypt(password));
		users.setUserType("member");
		usersMapper.insert(users);
		Long userId = users.getUserId();
		userPay.setPuid(userId);
		// 插入钱包
		String walletAddress = userPay.getWalletAddress();
		if (!StringUtils.isEmpty(walletAddress)) {
			if (!CoinValidationUtil.bitCoinAddressValidate(walletAddress)) {
				throw new AccountException("wallet.address.error", "钱包地址无效");
			}
		}
		//
		btcUserPayService.insert(userPay);
		//
		// 权限
		userRoleService.saveUserRole(users.getUserId(), 6);
		// 区域
		userRegion.setUserId(users.getUserId());
		userRegionSerice.seva(userRegion);
		// 记录日志
		UserRegLog entity = new UserRegLog();
		entity.setCreateBy(0L);
		entity.setCreateTime(new Date());
		entity.setDesc("创建子账号");
		entity.setRegIp(AccountSecurityUtils.getHost());
		entity.setUserId(users.getUserId());
		userRegLogMapper.insert(entity);
	}

	@Override
	public List<User> findSubAccount(String userId) {
		if (userId != null) {
			return usersMapper.findSubAccount(userId);
		} else {
			return null;
		}

	}

	@Override
	public List<User> findSubAccountByCurrencyId(Long userId) {
		if (userId != null) {
			return usersMapper.findSubAccountByCurrencyId(userId, multipleProperties.getId());
		} else {
			return null;
		}
	}

	@Override
	public User switchSubAccount(Long userId) {
		User switchUser = usersMapper.selectById(userId);

		User currentUser = AccountSecurityUtils.getUser();
		Integer currentRole = userRoleService.findUserRoleId(currentUser.getUserId());
		if (switchUser != null) {
			if (currentRole.equals(1)) {
				if (!currentUser.getUserId().equals(userId)
						&& (switchUser.getMasterUserId() == null || switchUser.getMasterUserId().equals(currentUser.getUserId()))) {
					AccountSecurityUtils.getSubject().runAs(new SimplePrincipalCollection(switchUser, ""));
					AccountSecurityUtils.getSubject().getSession().setAttribute("master", currentUser.getUserId());
				} else {
					if (log.isDebugEnabled()) {
						log.debug("master switch subaccount master:{} <--> MasterUserId:{} switch:{} ",
								currentUser.getUserId(), switchUser.getMasterUserId(), switchUser.getUserId());
					}
					throw new AccountException("401", "权限不足");
				}
			} else if (currentRole.equals(6)) {
				if (switchUser.getUserId()
						.equals(AccountSecurityUtils.getSubject().getSession().getAttribute("master"))) {
					AccountSecurityUtils.getSubject().runAs(new SimplePrincipalCollection(switchUser, ""));
				} else {
					if (log.isDebugEnabled()) {
						log.debug("subaccount switch master {} <--> {}, currentRole={}, curId={}", switchUser.getUserId(),
								AccountSecurityUtils.getSubject().getSession().getAttribute("master"), currentRole, currentUser.getUserId());
					}
					throw new AccountException("401", "权限不足");
				}
			} else {
				throw new AccountException(ExceptionCode.switch_sub_account_ex);
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("switch user null");
			}
			throw new AccountException(ExceptionCode.switch_sub_account_ex);
		}

		switchUser.setPassword(null);
		return switchUser;
	}

	@Override
	public IPage<SubAccountList> findSubAccountInfo(IPage<SubAccountList> page, User userParam, String currencyname) {
		Integer currencyId = currencyService.selectOneByCurrencyName(currencyname).getId();
		IPage<SubAccountList> selectPage = usersMapper.findUserByMaster(page, userParam, currencyId);
		// 算力
		List<SubAccountList> records = selectPage.getRecords();

		List<Long> users = new ArrayList<>();
		for(SubAccountList sub : records){
			users.add(sub.getUserId());
		}

		List<Map<String, Object>> uidList = btcUserPayService.getUserPayByIdWithoutCid(users, currencyId);

		for(SubAccountList sub : records){
			Long tmpUid = sub.getUserId();
			for(Map<String, Object> tmpMap : uidList){
				Long u = Long.parseLong(tmpMap.get("uid").toString());
				if(u.equals(tmpUid)){
					sub.addOtherCurrency(tmpMap);
				}
			}
		}

		if(!records.isEmpty()) {
			List<Currency> currencies = currencyService.selectListByEnable();

			for(Currency currency : currencies) {
				if(!currency.getType().equals(currencyname)){
					continue;
				}
				Map<Long, SubAccountList> tmp = new HashMap<>();
				List<Long> userIds = new ArrayList<>();
				for (SubAccountList subAccountList : records) {
					if(currency.getType().equals(subAccountList.getWalletType())){
						tmp.put(subAccountList.getUserId(), subAccountList);
						userIds.add(subAccountList.getUserId());
					}
				}
				if(userIds.isEmpty()){
					continue;
				}
				if(currency.getId().equals(1)) { //btc
					List<MiningWorkers> miningWorkersList = poolMapper.getMiningWorkersByPuidList(userIds);
					for (MiningWorkers miningWorkers : miningWorkersList) {
						Long puid = miningWorkers.getPuid();
						SubAccountList userStatus = tmp.get(puid);
						userStatus.setCurrentShareT(miningWorkers.getHashAccept15mT());
						userStatus.setHashAccept5mT(miningWorkers.getHashAccept5mT());
					}

					List<CurrentWorkerStatus> countUserWorkerStatusBatch = workerService.countUserWorkerStatusBatch(userIds);
					for (CurrentWorkerStatus currentWorkerStatus : countUserWorkerStatusBatch) {
						Long userId = currentWorkerStatus.getUserId();
						Integer count = currentWorkerStatus.getCount();
						Integer workerStatus = currentWorkerStatus.getWorkerStatus();

						SubAccountList userStatus = tmp.get(userId);
						int workerTotal = userStatus.getWorkerTotal();
						if (WorkerStatus.active.getStatus().equals(workerStatus)) {
							userStatus.setWorkerActive(count);
						}
						workerTotal += count;
						userStatus.setWorkerTotal(workerTotal);
					}
				}else{
					if(!userIds.isEmpty()) {
						MultipleShare multipleShare = rpcInstance.getRpcInstanceByCurrencyId(currency.getId());
						if(multipleShare != null) {
							String ids = userIds.toString();
							String result = multipleShare.getWorkersStatus(ids);
							if (result != null) {
								Gson gson = GsonUtil.getGson();
								Map<String, Object> data = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
								}.getType());
								List<Map<String, Object>> workerStatusData = (List<Map<String, Object>>) data.get("WorkerStatus");
								if (workerStatusData != null) {
									for (Map<String, Object> map : workerStatusData) {
										String idStr = map.get("userId").toString();
										Long userId = Long.parseLong(idStr);
										Integer count = Integer.parseInt(map.get("count").toString());
										Integer workerStatus = Integer.parseInt(map.get("workerStatus").toString());

										SubAccountList userStatus = tmp.get(userId);
										int workerTotal = userStatus.getWorkerTotal();
										if (WorkerStatus.active.getStatus().equals(workerStatus)) {
											userStatus.setWorkerActive(count);
										}
										workerTotal += count;
										userStatus.setWorkerTotal(workerTotal);
									}
								}

								List<Map<String, Object>> MiningWorkersData = (List<Map<String, Object>>) data.get("MiningWorkers");
								if (MiningWorkersData != null) {
									for (Map<String, Object> map : MiningWorkersData) {
										Long puid = Long.parseLong(map.get("puid").toString());
										SubAccountList userStatus = tmp.get(puid);
										Long ha15 = Long.parseLong((map.get("accept15m").toString()));
										Long ha5 = Long.parseLong((map.get("accept5m").toString()));
										MiningWorkers miningWorkers = new MiningWorkers();
										miningWorkers.setAccept15m(ha15);
										miningWorkers.setAccept5m(ha5);
										userStatus.setCurrentShareT(miningWorkers.getHashAccept15mT());
										userStatus.setHashAccept5mT(miningWorkers.getHashAccept5mT());
									}
								}
							}
						}
					}

				}
			}
		}
		return selectPage;
	}

	@Override
	public void insert(User entity) {
		usersMapper.insert(entity);
	}

	@Override
	public void inserts(List<User> entitys) {
		for (User user : entitys) {
			insert(user);
		}
	}

	@Override
	public void update(User entity) {
		usersMapper.updateById(entity);
	}

	@Override
	public void delete(Serializable id) {
		usersMapper.deleteById(id);
	}

	@Override
	public User findById(Serializable id) {
		return usersMapper.selectById(id);
	}

	@Override
	public void updatePassword(String username, String oldPassword, String newPassword) {
		User user = usersMapper.findByUsername(username);
		if (user.getPassword().equals(EncryUtil.encrypt(oldPassword))) {
			user.setPassword(EncryUtil.encrypt(newPassword));
			usersMapper.updateById(user);
		} else {
			throw new AccountException(ExceptionCode.password_error);
		}
		SecurityUtils.getSubject().logout();
	}

	@Override
	public void updateEmail(String username, String email) {
		User user = usersMapper.findByUsername(username);
		user.setUserEmail(email);
		usersMapper.updateById(user);
		User user2 = AccountSecurityUtils.getUser();
		user2.setUserEmail(email);
	}

	@Override
	public String sendLoginSmsCode(String username, String phoneNumber) {
		String code = UUIDUtil.generateNumber(6);
		String key = RedisKeys.getLoginSmsCode(username, phoneNumber);
		phoneCodeCahceService.putCode(key, code);
		smsService.sendSmsCode(phoneNumber, code, phoneNumber);
		return code;
	}

	@Override
	public String sentRegSmsCode(String phoneNumber) {
		User user = usersMapper.findByPhone(phoneNumber);
		if (user != null) {
			throw new AccountException(ExceptionCode.phone_exists);
		}
		String code = UUIDUtil.generateNumber(6);
		String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
		String key = phoneCodeCahceService.builderKey(sessionId, code);
		phoneCodeCahceService.putCode(key, code);
		try {
			smsService.sendSmsCode(phoneNumber, code, phoneNumber);
		} catch (Exception e) {
			log.error("", e);
		}

		return code;
	}

	@Override
	public String sentRestPasswordSmsCode(String phoneNumber) {
		User user = usersMapper.findByPhone(phoneNumber);
		if (user == null) {
			throw new AccountException(ExceptionCode.phone_not_exists);
		}
		String outId = phoneNumber;
		String code = UUIDUtil.generateNumber(6);
		String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
		String key = phoneCodeCahceService.builderKey(sessionId, code);
		phoneCodeCahceService.putCode(key, code);
		try {
			smsService.sendSmsCode(phoneNumber, code, outId);
		} catch (Exception e) {
			log.error("", e);
		}
		return code;
	}

	@Override
	public void updatePhone(String username, String phone) {
		User user = usersMapper.findByUsername(username);
		user.setUserPhone(phone);
		usersMapper.updateById(user);
		User user2 = AccountSecurityUtils.getUser();
		user2.setUserPhone(phone);
	}

	@Override
	public void updateUserInfo(UserInfo userInfo) {
		String password = userInfo.getPassword();
		String username = userInfo.getUsername();
		User user = usersMapper.findByUsername(username);
		if (user.getPassword().equals(EncryUtil.encrypt(password))) {
			if (userInfo.getEmail() != null) {
				user.setUserEmail(userInfo.getEmail());
			}
			if (userInfo.getPhone() != null) {
				user.setUserPhone(userInfo.getPhone());
			}
			usersMapper.updateById(user);
		} else {
			throw new AccountException(ExceptionCode.password_error);
		}
		User userCurr = AccountSecurityUtils.getUser();
		if (userInfo.getEmail() != null) {
			userCurr.setUserEmail(userInfo.getEmail());
		}
		if (userInfo.getPhone() != null) {
			userCurr.setUserPhone(userInfo.getPhone());
		}
	}

	@Override
	public String sendCodeByPhone(String phone) {

		User user = usersMapper.findByPhone(phone);
		if (user != null) {
			throw new AccountException(ExceptionCode.phone_exists);
		}
		String code = UUIDUtil.generateNumber(6);
		String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
		String key = phoneCodeCahceService.builderKey(sessionId, code);
		phoneCodeCahceService.putCode(key, code);
		try {
			smsService.sendSmsCode(phone, code, phone);
		} catch (Exception e) {
			log.error("", e);
		}
		return code;
	}

	@Override
	public IPage<User> selectUserPage(IPage<User> iPage, User user) {
		return usersMapper.selectPage(iPage, new QueryWrapper<User>(user));
	}

	@Override
	public User getCurrentUser() {
		User user = AccountSecurityUtils.getUser();
		if (user.getUserEmail() == null && user.getUserPhone() == null) {
			if (user.getMasterUserId() != null) {
				User mastertuser = usersMapper.selectById(user.getMasterUserId());
				user.setUserEmail(mastertuser.getUserEmail());
				user.setUserPhone(mastertuser.getUserPhone());
			}
		}
		user.setPassword(null);

		return user;
	}

	@Override
	public void updateSubUserPassword(Long userId, String newPassword) {
		User user = usersMapper.selectById(userId);
		if (user == null) {
			throw new AccountException("master.update.password", "修改密码错误");
		}
		Long masterUserId = AccountSecurityUtils.getUser().getUserId();
		if (user.getMasterUserId() == null || !user.getMasterUserId().equals(masterUserId)) {
			throw new AccountException("master.update.password", "修改密码错误");
		}
		User user2 = new User();
		user2.setPassword(EncryUtil.encrypt(newPassword));
		user2.setUserId(userId);
		usersMapper.updateById(user2);
	}

	@Override
	public String sendSecurityCodeEmail(String email) {
		String code = UUIDUtil.generateNumber(6);
		String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
		String key = emailCodeCacheService.builderKey(sessionId, code);
		emailCodeCacheService.putCode(key, code);
		mailService.sendSimple(email, "安全验证码", code);
		return code;
	}

	@Override
	public String sendSecurityCodePhone(String phone) {
		String code = UUIDUtil.generateNumber(6);
		String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
		String key = phoneCodeCahceService.builderKey(sessionId, code);
		phoneCodeCahceService.putCode(key, code);
		try {
			smsService.sendSmsCode(phone, code, phone);
		} catch (Exception e) {
			log.error("", e);
		}
		return code;
	}

	@Override
	public IPage<User> selectUserPageByPayCurrencyId(IPage<User> iPage, Long masterId) {
		return usersMapper.selectUserPageByPayCurrencyId(iPage, masterId);
	}
}
