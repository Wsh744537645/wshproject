package com.mpool.share.config.shiro.realm;

import com.mpool.share.config.shiro.token.JWTToken;
import com.mpool.share.exception.Exception401;
import com.mpool.share.model.User;
import com.mpool.share.service.UserService;
import com.mpool.share.utils.JWTUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: stephen
 * @Date: 2019/6/12 9:51
 */

@Component
public class JWTRealm extends AuthorizingRealm {
    @Autowired
    private UserService usersService;

    @Override
    public boolean supports(AuthenticationToken token){
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getPrincipal();
        if(token == null){
            throw new AuthenticationException("token invalid");
        }

        String username = JWTUtils.getUsername(token);
        if(username == null){
            throw new AuthenticationException("token invalid");
        }

        if(JWTUtils.isTokenExpired(token)){
            throw new Exception401();
        }

        User user = usersService.findByUsername(username);
        if(user == null){
            throw new AuthenticationException("user didn't existed!");
        }

        if(!JWTUtils.verify(token, username, user.getPassword())){
            throw new AuthenticationException("username or password error");
        }

        return new SimpleAuthenticationInfo(user, token, getName());
    }
}
