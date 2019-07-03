package com.mpool.account.controller;

import com.google.gson.Gson;
import com.mpool.account.exception.AccountException;
import com.mpool.account.exception.ExceptionCode;
import com.mpool.account.utils.AccountSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.account.User;
import com.mpool.common.util.GsonUtil;
import com.mpool.rpc.RpcInstance;
import com.mpool.rpc.account.producer.btc.MultipleShare;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 此处开始访问多币种服务，后续可加入服务发现和服务治理。
 */

@RestController
@RequestMapping({"/mutiple", "/apis/mutiple"})
public class MutipleController {
    private static final Logger log = LoggerFactory.getLogger(MutipleController.class);

    @Autowired
    private RpcInstance rpcInstance = null;

    //http://127.0.0.1:8079/mutiple/ZEC/?current=1&size=10&status=active&id=cb16d726446e41aa845ecd845e6e59cb
    @RequestMapping("/{coinType}")
    public Result sendMutipleService(@PathVariable(value = "coinType") String coinType, @RequestParam(value = "url")String url, HttpServletRequest request){
        Enumeration<String> enumerations =  request.getParameterNames();
        Map<String, Object> map = new HashMap<>();
        map.put("curSessionId", request.getSession().getId());
        map.put("coinType", coinType);
        while (enumerations.hasMoreElements()) {
            String key = enumerations.nextElement();
            Object value = request.getParameter(key);
            map.put(key, value);
        }
        Gson gson = GsonUtil.getGson();
        Result result;
        MultipleShare multipleShare = rpcInstance.getRpcInstanceByCurrencyType(coinType);
        if(multipleShare != null) {
            if(SecurityUtils.getSubject().getPrincipals() != null) {
                result = multipleShare.multipleConsumer(AccountSecurityUtils.getUser(), gson.toJson(map));
            }else{
                result = multipleShare.multipleConsumer(new User(), gson.toJson(map));
            }
        }else {
            log.error("该多币种 {} service不存在！！！", coinType);
            throw new AccountException(ExceptionCode.multiple_service_not_exists);
        }
        return result;
    }
}
