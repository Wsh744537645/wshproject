package com.mpool.account.program.Service.impl;

import com.mpool.account.config.shiro.token.UsernamePasswordTelphoneToken;
import com.mpool.account.mapper.UserMapper;
import com.mpool.account.mapper.UserRoleMapper;
import com.mpool.account.program.Service.LoginService;
import com.mpool.account.program.controller.WxMaUserController;
import com.mpool.account.program.mapper.OPenidMapper;
import com.mpool.account.program.mapper.WxUserMapper;
import com.mpool.account.utils.AccountSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.account.User;
import com.mpool.common.model.program.WxOpenid;
import com.mpool.common.util.EncryUtil;
import com.mpool.common.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    WxMaUserController wxMaUserController;
    @Autowired
    WxUserMapper wxUserMapper;
    @Autowired
    OPenidMapper oPenidMapper;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Override
    public String loginWeixin(String username, String password) {

        if (username != null && password != null) {
            User user = userMapper.findByUsername(username);
          Integer roleId = userRoleMapper.findUserRoleId(user.getUserId());
            if (user == null) {
                throw new RuntimeException("用户信息不存在，请核对后再输入！");
            }
            //Base64解密密码参数
//            byte[] str = password.getBytes();
//            String strpswd = Base64.getEncoder().encodeToString(str);
//            String pswd1 = new String(Base64.getDecoder().decode(strpswd));
            //数据库密码解密
            String pawd = user.getPassword();
            String decodePswd2 = EncryUtil.decrypt(pawd);
            //校验密码是否正确
            if (password.equals(decodePswd2)) {
                user.setLastTime(new Date());
//                更新最后登录时间
                userMapper.updateById(user);

                Map map = new HashMap();
                map.put("username", username);
                map.put("password", password);
                //生成token
                String token = JWTUtil.sign(map);
                log.debug("微信小程序用户登录成功->token:" + token);
                return "Ok";
            } else {
                throw new RuntimeException("您输入的账号或密码有误，请核对后再输入！");
            }
        }
        log.debug("微信小程序用户登录失败username:" + username);
        return null;
    }

    @Override
    public User getUserInfoByToken(String token) {
        boolean verify = JWTUtil.verify(token);
        if (!verify) {
            throw new RuntimeException("token无效");
        } else {
            //JWT解密获取token里面的username
            Map<String, String> tokenInfo = JWTUtil.getTokenValue(token);
            String user = tokenInfo.get("username");
            User userInfo = userMapper.findByUsername(user);
            return userInfo;
        }
    }

    /**
     * wgg
     * 微信小程序 openid绑定用户
     *
     * @param username
     * @param password
     * @param openid
     * @return
     */
    @Override
    public Map bindingUser(String username, String password, String openid) {
        Map<String,String> resultCode = new HashMap();
        if (username == null || password == null || openid == null) {
            resultCode.put("1","用户名或密码不能为空");
            return resultCode;
        }
        User user = userMapper.findByUsername(username);
        if (user == null){
            resultCode.put("code","未找到该用户");
            return resultCode;
        }
        String userType = user.getUserType();
        if(userType.equals("master")){
            String pswd = user.getPassword();
            String decodePswd = EncryUtil.decrypt(pswd);
            if (password.equals(decodePswd)) {
                //用户密码正确，则可以保存openid了
                User users = wxUserMapper.getUserInfoByOpenid(openid);
                if (users==null){
                    WxOpenid wxOpenid = new WxOpenid();
                    wxOpenid.setOpenid(openid);
                    wxOpenid.setUserid(user.getUserId());
                    wxOpenid.setAddtime(new Date());
                    //保存openid
                    oPenidMapper.insert(wxOpenid);
                    resultCode.put("code","0");
                    return resultCode;
                }else {
                    resultCode.put("code","该用户已绑定过用户");
                    return resultCode;
                }
            }else {
                resultCode.put("code","密码不正确");
                return resultCode;
            }
        }else {
            resultCode.put("code","非主账号");
            return resultCode;
        }
    }

    /**
     * wgg
     * 微信小程序 解绑用户
     *
     * @param username password
     * @return
     */
    @Override
    public Map deleteBinding(String username, String password, String openid) {
        Map map = new HashMap();
        if (username == null || password == null) {
            map.put("resultCode", "err");
        } else {
            User user = userMapper.findByUsername(username);
            if (user == null) {
                map.put("resultCode", "err");
                return map;
            }
            String pswd = user.getPassword();
            String decodePswd = EncryUtil.decrypt(pswd);
            if (password.equals(decodePswd)) {
                //用户密码正确，则可以解绑（删除openid）
                oPenidMapper.deleteBinding(openid);
                //登出
                SecurityUtils.getSubject().logout();
                map.put("resultCode", "ok");
                log.debug("username", username + "解绑成功！");
            } else {
                map.put("resultCode", "err");
            }
        }
        return map;
    }

    /**
     * wgg
     * 微信小程序
     *
     * @param openid
     * @return
     */
    @Override
    public Map wxLogin(String openid) {
        Map map = new HashMap();
        if (openid != null) {
            User user = wxUserMapper.getUserInfoByOpenid(openid);
            //检查openid是否绑定主账号User信息
            if (user == null) {
                map.put("openid",openid);
                return map;
            }
            //获取当前账号角色（主\子）
            Integer roleId = userRoleMapper.findUserRoleId(user.getUserId());
            //如果通过openid拿到了用户信息则进行登录操作
            String username = user.getUsername();
            String pswd = user.getPassword();
            //从数据库获取的密码需要解密
            String decodePswd = EncryUtil.decrypt(pswd);
            Session session = AccountSecurityUtils.getSubject().getSession();
            UsernamePasswordTelphoneToken token = new UsernamePasswordTelphoneToken(username, decodePswd);
            Subject subject = AccountSecurityUtils.getSubject();
            subject.login(token);
            user.setLastIp(session.getHost());
            //更新最后登录时间
            user.setLastTime(new Date());
            userMapper.updateById(user);
            //获取sessionid返回 给前端会话
            String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
           String name = user.getUsername();
            map.put("username",name);
            map.put("sessionId", sessionId);
            map.put("openid",openid);
            map.put("role",roleId);
            return map;
        }
        return null;
    }

    @Override
    public String reWxLogin(String username, String password) {
        if (username == null || password == null) {
            throw new RuntimeException("您输入的账号密码不完整！");
        } else {
            User user = userMapper.findByUsername(username);
            String pswd = user.getPassword();
            String decodePswd = EncryUtil.decrypt(pswd);
            if (password.equals(decodePswd)) {
                //用户密码正确，则可以登录
                Session session = AccountSecurityUtils.getSubject().getSession();
                UsernamePasswordTelphoneToken token = new UsernamePasswordTelphoneToken(username, decodePswd);
                Subject subject = AccountSecurityUtils.getSubject();
                subject.login(token);
                user.setLastIp(session.getHost());
                //更新最后登录时间
                user.setLastTime(new Date());
                userMapper.updateById(user);
                //获取sessionid返回给前端会话
                String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
                return sessionId;
            }
            return null;
        }
    }

}
