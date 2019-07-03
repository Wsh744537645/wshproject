package com.mpool.admin.utils;

import com.mpool.admin.exception.AdminException;
import com.mpool.common.model.admin.SysUser;
import org.apache.shiro.SecurityUtils;

import com.mpool.common.model.account.User;
import org.apache.shiro.authc.AccountException;

public class AccountSecurityUtils extends SecurityUtils {
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

    public static String getHost() {
        return getSubject().getSession().getHost();
    }
}
