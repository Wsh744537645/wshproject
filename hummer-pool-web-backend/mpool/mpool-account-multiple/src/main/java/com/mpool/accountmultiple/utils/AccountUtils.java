package com.mpool.accountmultiple.utils;

import com.mpool.common.model.account.User;
import org.apache.shiro.session.Session;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class AccountUtils {
    private final static ThreadLocal<User> user = new ThreadLocal<>();
    private final static ThreadLocal<String> sessionID = new ThreadLocal<>();

    public static void setUser(User user){
        AccountUtils.user.set(user);
    }

    public static User getUser(){
        return AccountUtils.user.get();
    }

    public static Long getCurrentUserId(){
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

    public static void setSessionID(String sessionID){
        AccountUtils.sessionID.set(sessionID);
    }

    public static String getSessionID(){
        return AccountUtils.sessionID.get();
    }
}
