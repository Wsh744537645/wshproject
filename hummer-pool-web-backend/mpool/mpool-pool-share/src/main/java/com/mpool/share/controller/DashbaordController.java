package com.mpool.share.controller;

import com.mpool.common.CoinValidationUtil;
import com.mpool.common.Result;
import com.mpool.common.cache.aspect.annotation.CheckCodeEmail;
import com.mpool.common.cache.aspect.annotation.CheckCodeSms;
import com.mpool.share.model.User;
import com.mpool.share.model.UserNotify;
import com.mpool.share.service.UserNotifyService;
import com.mpool.share.service.UserPayService;
import com.mpool.share.service.UserService;
import com.mpool.share.utils.AccountSecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.mpool.share.constant.Constant.*;

/**
 * @Author: stephen
 * @Date: 2019/5/31 10:05
 */

@RestController
@RequestMapping({"/dashbaord", "apis/dashbaord"})
public class DashbaordController {
    @Autowired
    private UserNotifyService notifyService;
    @Autowired
    private UserService usersService;
    @Autowired
    private UserPayService userPayService;

    @GetMapping("/info/notify")
    @ApiOperation("获得用户到期提醒信息")
    public Result getUserNotifyInfo(){
        Long userId = AccountSecurityUtils.getUser().getUserId();
        UserNotify notify = notifyService.findOne(userId);
        return Result.ok(notify);
    }

    @PostMapping("/notify/update")
    @ApiOperation("设置用户提醒信息")
    public Result userNotifySetting(@RequestBody UserNotify notify){
        Long userId = AccountSecurityUtils.getUser().getUserId();
        UserNotify result = notifyService.findOne(userId);
        result.setUserNotify(notify);
        notifyService.updateUserNotify(result);
        return Result.ok(result);
    }

    @GetMapping("/get/user")
    @ApiOperation("获取用户信息")
    public Result getUserInfo(){
        Map<String, Object> map = new HashMap<>();
        User user = usersService.findByUserId(AccountSecurityUtils.getUser().getUserId());
        map.put("uid", user.getUserId());
        map.put("username", user.getUsername());
        map.put("phone", user.getUserPhone());
        map.put("mail", user.getUserEmail());
        map.put("walletAddress", userPayService.getUserWalletAddress());
        return Result.ok(map);
    }

    @GetMapping("/reset/email/code")
    @ApiOperation("发送邮箱验证码-修改邮箱")
    public Result getResetEmailCode(String mail){
        usersService.sendCodeByResetMail(mail);
        return Result.ok();
    }

    @PostMapping("/reset/email")
    @ApiOperation("邮箱修改")
    @CheckCodeEmail(prefix = CODE_MAIL_SETTING_PREFIX)
    public Result resetEmail(@RequestBody @Validated @Email String mail){
        usersService.checkExistByMail(mail);
        usersService.updateMailByUserId(AccountSecurityUtils.getUser().getUserId(), mail);
        return Result.ok();
    }

    @GetMapping("/reset/phone/code")
    @ApiOperation("发送手机验证码-修改手机号")
    public Result getRestPhoneCode(String phone){
        usersService.sendCodeByRestPhone(phone);
        return Result.ok();
    }

    @PostMapping("/reset/phone")
    @ApiOperation("手机号修改")
    @CheckCodeSms(prefix = CODE_PHONE_SETTING_PREFIX)
    public Result resetPhone(@RequestBody String phone){
        usersService.checkExistByPhone(phone);
        usersService.updatePhoneByUserId(AccountSecurityUtils.getUser().getUserId(), phone);
        return Result.ok();
    }

    @GetMapping("/reset/wallet/code")
    @ApiOperation("发送验证码-修改钱包地址")
    public Result getRestWalletAddressCode(){
        User user = usersService.findByUserId(AccountSecurityUtils.getUser().getUserId());
        int type = 1; //1：手机号发送，2：邮件发送
        if(user.getUserPhone() != null && !user.getUserPhone().isEmpty()){
            usersService.sendPhoneCodeByWalletAddress(user.getUserPhone());
        }else {
            type = 2;
            usersService.sendMailCodeByWalletAddress(user.getUserEmail());
        }

        Map<String, Object> map = new HashMap<>(1);
        map.put("type", type);
        return Result.ok(map);
    }

    @PostMapping("/reset/wallet/phone")
    @ApiOperation("修改钱包地址-手机号验证")
    @CheckCodeSms(prefix = CODE_PHONE_WALLET_PREFIX)
    public Result resetWalletAddressPhone(@RequestBody String address){
        usersService.updateWalletAddress(AccountSecurityUtils.getUser().getUserId(), address);
        return Result.ok();
    }

    @PostMapping("/reset/wallet/email")
    @ApiOperation("钱包地址修改-邮箱验证")
    @CheckCodeEmail(prefix = CODE_MAIL_WALLET_PREFIX)
    public Result resetWalletAddressEmail(@RequestBody String address){
        usersService.updateWalletAddress(AccountSecurityUtils.getUser().getUserId(), address);
        return Result.ok();
    }

    @GetMapping("/reset/password/code")
    @ApiOperation("发送验证码-修改用户密码")
    public Result getRestPasswordCode(){
        User user = usersService.findByUserId(AccountSecurityUtils.getUser().getUserId());
        int type = 1; //1：手机号发送，2：邮件发送
        if(user.getUserPhone() != null && !user.getUserPhone().isEmpty()){
            usersService.sendPhoneCodeByPassward(user.getUserPhone());
        }else {
            type = 2;
            usersService.sendMailCodeByPassward(user.getUserEmail());
        }

        Map<String, Object> map = new HashMap<>(1);
        map.put("type", type);
        return Result.ok(map);
    }

    @PostMapping("/reset/password/phone")
    @ApiOperation("用户密码修改-手机号验证")
    @CheckCodeSms(prefix = CODE_PHONE_PASSWORD_PREFIX)
    public Result resetPasswordPhone(@RequestBody String password){
        usersService.updatePassward(AccountSecurityUtils.getUser().getUserId(), password);
        return Result.ok();
    }

    @PostMapping("/reset/password/email")
    @ApiOperation("用户密码修改-邮箱验证")
    @CheckCodeEmail(prefix = CODE_MAIL_PASSWORD_PREFIX)
    public Result resetPasswordEmail(@RequestBody String password){
        usersService.updatePassward(AccountSecurityUtils.getUser().getUserId(), password);
        return Result.ok();
    }

    @GetMapping("/walletAddress/validated")
    @ApiOperation("钱包地址校验")
    @ResponseBody
    public Result walletAddressValidated(@RequestParam("walletAddress") String walletAddress) throws IOException {
        boolean bitCoinAddressValidate = CoinValidationUtil.bitCoinAddressValidate(walletAddress);
        Map<String, Object> result = new HashMap<>(1);
        if (!bitCoinAddressValidate) {
            result.put("result", 0);
        }else{
            result.put("result", 1);
        }
        return Result.ok(result);
    }
}
