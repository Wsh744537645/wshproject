package com.mpool.account.program.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.mpool.account.exception.AccountException;
import com.mpool.account.mapper.UserMapper;
import com.mpool.account.model.dashboard.UserStatus;
import com.mpool.account.program.Service.LoginService;
import com.mpool.account.program.common.config.WxMaConfiguration;
import com.mpool.account.program.common.config.utils.JsonUtils;
import com.mpool.account.service.PoolService;
import com.mpool.account.utils.AccountSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.account.User;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wx/user")
public class WxMaUserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoginService loginService;

    @Autowired
    private PoolService poolService;
    @Autowired
    private UserMapper userMapper;

    /**
     * 登陆接口
     */
    @GetMapping("/login")
    @ApiOperation("此接口获取Openid，传至/wxLogin接口进行登录")
    public Result login(@RequestParam(value = "code") String code) {
        String appid = "wx94fa46b3614974b8";
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            this.logger.info(session.getOpenid());
            //TODO 可以增加自己的逻辑，关联业务相关数据
            String openid = session.getOpenid();
            //获取sessionId
            Map map = loginService.wxLogin(openid);
            return Result.ok(map);
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
        }
        throw AccountException.userNotfound();
    }


    /**
     * <pre>
     * 获取用户信息接口  --《备用接口》
     * </pre>
     */
//    @GetMapping("/info")
//    @ApiOperation("获取用户信息接口")
    public String info(@PathVariable String appid, String sessionKey,
                       String signature, String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);
        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }
        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        String str = JsonUtils.toJson(userInfo);
        return str;
    }


    @PostMapping("/bindingUser")
    @ApiOperation("绑定用户<String username ,String password,String WxOpenid>")
    public Result bindingUser(@Valid @RequestBody User mode) {
        String username = mode.getUsername();
        String password = mode.getPassword();
        String openid = mode.getOpenid();
        //绑定插入用户openid数据
        Map<String,String> resultCode = loginService.bindingUser(username, password, openid);

        return Result.ok(resultCode);
    }

    @PostMapping("/deleteBinding")
    @ApiOperation("解绑用户<String username , String password>")
    public Result deleteBinding(@Valid @RequestBody User mode) {
        String username = mode.getUsername();
        String password = mode.getPassword();
        String openid = mode.getOpenid();
        //解绑删除解绑用户的OpenId数据
        Map map = loginService.deleteBinding(username, password, openid);
        return Result.ok(map);
    }

    @PostMapping("/wxLogin")
    @ApiOperation("测试接口->此接口需要传openid微信登录（检查是否绑定用户，未绑定需要跳转绑定页面，已绑定的则返回sessionid）")
    public Result wxLogin(@Valid @RequestBody User mode) {
        String openid = mode.getOpenid();
        Map map = loginService.wxLogin(openid);
        return Result.ok(map);
    }

    @PostMapping("/reWxLogin")
    @ApiOperation("sessionId失效后重新账号密码登录")
    public Result reWxLogin(@Valid @RequestBody User mode) {
        Map mapCode = new HashMap();
        String username = mode.getUsername();
        String password = mode.getPassword();
        String sessionId = loginService.reWxLogin(username, password);
        if (sessionId == null) {
            mapCode.put("err", "微信小程序登录失败，账户或密码输入错误！");
            logger.debug("username:" + username + "密码错误！");
        } else {
            mapCode.put("ok", "微信小程序登录成功！");
            logger.debug("username:" + username + "+" + sessionId + "微信小程序登录成功！");
        }
        return Result.ok(mapCode);
    }

    @GetMapping("/getWxSubRuntimeInfo")
    @ApiOperation("子账号获得自己的运行时数据")
    public Result getWxSubRuntimeInfo(@RequestParam(value = "userId") Long userId) {
        User user = null;
        user = userMapper.findById(userId);
        List<User> list = new ArrayList<>();
        list.add(user);
        UserStatus userStatus = poolService.getMasterRuntimeInfo(list).get(0);
        if (userStatus == null) {
            userStatus = new UserStatus();
        }
        return Result.ok(userStatus);
    }

    @GetMapping("/getWxSubUserInfo")
    @ApiOperation("子账号详情数据")
//    @RequiresPermissions("anon")
    public Result getWxSubUserInfo(@RequestParam(value = "userId") Long userId) {
        User user = null;
        user = userMapper.findById(userId);
        List<User> list = new ArrayList<>();
        list.add(user);
        UserStatus userStatus = poolService.getMasterRuntimeInfo(list).get(0);
        if (userStatus == null) {
            userStatus = new UserStatus();
        }
        return Result.ok(userStatus);
    }
}
