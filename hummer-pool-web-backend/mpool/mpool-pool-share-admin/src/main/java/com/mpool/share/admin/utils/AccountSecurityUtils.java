package com.mpool.share.admin.utils;

import com.mpool.share.admin.exception.AdminException;
import com.mpool.common.model.admin.SysUser;
import org.apache.shiro.SecurityUtils;

public class AccountSecurityUtils extends SecurityUtils {
    public static SysUser getUser() {
        SysUser user = null;

        user = new SysUser();
        user.setUserId(1L);
        return user;
//        try {
//            user = (SysUser) getSubject().getPrincipals().getPrimaryPrincipal();
//            if (user == null) {
//                throw new AdminException("-1", "err");
//            }
//        } catch (Exception e) {
//            throw new AdminException("-1", "err");
//        }
//        return user;
    }

    public static String getHost() {
        return getSubject().getSession().getHost();
    }
}
