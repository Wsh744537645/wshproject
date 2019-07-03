package com.mpool.share.config.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpool.common.Result;
import com.mpool.share.config.shiro.token.JWTToken;
import com.mpool.share.exception.Exception401;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: stephen
 * @Date: 2019/6/12 10:08
 */

@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    ObjectMapper mapper = new ObjectMapper();

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response){
        HttpServletRequest req = (HttpServletRequest) request;
        String auth = req.getHeader("token");
        return auth != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String auth = req.getHeader("token");

        JWTToken token = new JWTToken(auth);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(isLoginAttempt(request, response)){
            try{
                return executeLogin(request, response);
            }catch (Exception e){
                if(e instanceof Exception401){

                }else{
                    return false;
                }
            }
        }
        response401(request, response);
        return false;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
    
    private void response401(ServletRequest req, ServletResponse resp) {
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            ((HttpServletResponse) resp).setStatus(401);
            out = resp.getWriter();
            out.write(mapper.writeValueAsString(Result.err401()));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
