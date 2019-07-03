package com.mpool.share.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @Author: stephen
 * @Date: 2019/6/20 18:42
 */
public class Exception401 extends AuthenticationException {
    public Exception401(){
        super("Authentication failure!");
    }
}
