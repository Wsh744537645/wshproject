package com.mpool.account.utils;

import org.apache.shiro.SecurityUtils;

import com.mpool.account.exception.AccountException;
import com.mpool.common.model.account.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class AccountSecurityUtils extends SecurityUtils {
	public static User getUser() {
		User user = null;
//		user = new User();
//		user.setUserId(11L);
//		return user;
		try {
			user = (User) getSubject().getPrincipals().getPrimaryPrincipal();
			if (user == null) {
				throw new AccountException("-1", "err");
			}
		} catch (Exception e) {
			throw new AccountException("-1", "err");
		}
		return user;
	}

	static public Long getCurrentUserId(){
		HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest();
		Long shareAddUserId = (Long) request.getAttribute("shareAddUserId");
		Long userId;
		if(shareAddUserId == null){
			// 获取当前登录用户
			User user = getUser();
			userId = user.getUserId();
		}else{
			userId = shareAddUserId;
		}
		return userId;
	}

	public static String getHost() {
		return getSubject().getSession().getHost();
	}

	public static String getCurPayCurrencyName(){
		String name = (String)AccountSecurityUtils.getSubject().getSession().getAttribute("currencyName");
		if(name == null || name.isEmpty()){
			name = "BTC";
		}
		return name;
	}
}
