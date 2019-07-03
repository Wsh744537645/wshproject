package com.mpool.share.admin.utils;

import com.mpool.share.admin.exception.AdminException;
import com.mpool.common.model.admin.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;

@Slf4j
public class AdminSecurityUtils extends SecurityUtils {
	public static SysUser getUser() {
		SysUser user = null;
		try {
			user = (SysUser) getSubject().getPrincipals().getPrimaryPrincipal();
			if (user == null) {
				throw new AdminException("-1", "err");
			}
		} catch (Exception e) {
			throw new AdminException("-1", "err");
		}
		return user;
	}

	public static void setCurrencyName(String currencyName){
		getSubject().getSession().setAttribute("currencyName", currencyName);
	}

	public static String getCurrencyName(){
		try {
			String currencyName = (String) getSubject().getSession().getAttribute("currencyName");
			if(currencyName == null){
				return "BTC";
			}
			return currencyName;
		}catch (Exception e){
			log.error("AdminSecurityUtils error.");
		}
		return "BTC";
	}
}
