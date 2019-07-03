package com.mpool.share.config.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Author: stephen
 * @Date: 2019/6/12 9:49
 */
public class JWTToken implements AuthenticationToken {

    //秘钥
    private String token;

    public JWTToken(String token){
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
