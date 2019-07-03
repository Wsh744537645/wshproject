package com.mpool.account.program.controller;

import com.mpool.account.exception.AccountException;
import com.mpool.account.exception.ExceptionCode;
import com.mpool.account.model.UserLoginModel;
import com.mpool.account.program.Service.LoginService;
import com.mpool.common.Result;
import com.mpool.common.model.account.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Base64;

/**
 * @author wgg
 */
@RestController
@RequestMapping({"/ProgUser", "apis/ProgUser"})
@Api("微信小程序用户管理Api")
public class ProgUserController {

    private static final Logger log = LoggerFactory.getLogger(com.mpool.account.controller.UserController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    @ApiOperation("微信小程序登录接口")
    public Result login(@Valid @RequestBody UserLoginModel loginInfo, HttpServletRequest request) {
        String username = loginInfo.getUsername();
        String password = loginInfo.getPassword();
        if (StringUtils.isEmpty(username)) {
            throw new AccountException(ExceptionCode.username_null_err);
        }
        if (StringUtils.isEmpty(password)) {
            throw new AccountException(ExceptionCode.password_error);
        }
       String token = loginService.loginWeixin(username,password);
        return Result.ok(token);
    }

    @GetMapping("/getUserInfoByToken")
    @ApiOperation("通过token获取用户信息")
    public Result getUserInfoByToken(@RequestParam("token")String token) {
        User userInfo = loginService.getUserInfoByToken(token);
        return Result.ok(userInfo);
    }


}
