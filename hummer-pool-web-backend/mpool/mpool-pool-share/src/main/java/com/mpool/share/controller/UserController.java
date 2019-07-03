package com.mpool.share.controller;

import com.mpool.common.MpoolKaptcha;
import com.mpool.common.QQCaptchaService;
import com.mpool.common.Result;
import com.mpool.common.cache.aspect.annotation.CheckCodeCaptcha;
import com.mpool.common.cache.aspect.annotation.CheckCodeEmail;
import com.mpool.common.cache.aspect.annotation.CheckCodeSms;
import com.mpool.common.cache.service.CaptchaCacheService;
import com.mpool.common.model.account.IpRegion;
import com.mpool.common.model.account.bill.UserPay;
import com.mpool.common.util.EncryUtil;
import com.mpool.share.config.shiro.token.JWTToken;
import com.mpool.share.model.User;
import com.mpool.common.model.account.log.UserLoginLog;
import com.mpool.common.model.log.LogUserOperation;
import com.mpool.common.util.UUIDUtil;
import com.mpool.share.constant.Constant;
import com.mpool.share.exception.ExceptionCode;
import com.mpool.share.exception.ShareException;
import com.mpool.share.mapper.UserIpAddessMapper;
import com.mpool.share.mapper.UserMapper;
import com.mpool.share.model.UserLoginModel;
import com.mpool.share.model.UserModel;
import com.mpool.share.model.UserRegModel;
import com.mpool.share.service.UserIpaddessService;
import com.mpool.share.service.UserLoginLogService;
import com.mpool.share.service.UserPayService;
import com.mpool.share.service.UserService;
import com.mpool.share.service.log.UserOperationService;
import com.mpool.share.utils.AccountSecurityUtils;
import com.mpool.share.utils.JWTUtils;
import com.mpool.share.utils.RequestResolveUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;

import static com.mpool.share.constant.Constant.CODE_REG_PREFIX;
import static com.mpool.share.constant.Constant.CODE_REST_PASSWORD_PREFIX;

/**
 * @Author: stephen
 * @Date: 2019/4/30 14:45
 */

@RestController
@RequestMapping({"/user", "apis/user"})
@Api("用户登录注册")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService usersService;
    @Autowired
    private CaptchaCacheService codeCacheService;

    @Autowired
    private UserLoginLogService userLoginLogService;
    @Autowired
    private MpoolKaptcha mpoolKaptcha;
    @Autowired
    private QQCaptchaService qqCaptchaService;
    @Autowired
    private UserIpaddessService userIpaddessService;
    @Autowired
    private UserIpAddessMapper userIpAddessMapper;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private UserOperationService userOperationService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserPayService userPayService;

    @PostMapping("/login")
    @ApiOperation("登录接口")
    //@CheckCodeCaptcha()
    public Result login(@Valid @RequestBody UserLoginModel loginModel, HttpServletRequest request) {
        log.info("--------> IP {}", request.getRemoteHost());
        String username = loginModel.getUsername();
        if (StringUtils.isEmpty(username)) {
            throw new ShareException(ExceptionCode.username_null_err);
        }

        String token = usersService.login(username, loginModel.getPassword(), loginModel.getTelphoneCode());
        User user = usersService.findByUsername(username);
//		final User user = AccountSecurityUtils.getUser();
        UserModel userModel = getUserInfo(user);
        userModel.setToken(token);
        String header = request.getHeader("User-Agent");
        // 记录登录日志
        executorService.execute(() -> {
            String ip = user.getLastIp();
            Long userId = user.getUserId();
            UserLoginLog entity = RequestResolveUtil.resolveUserLoninLog(ip,userId, header);
            IpRegion ipRegion =  userIpAddessMapper.getIpAddess(ip);
            if (ipRegion == null){
                entity.setCityAddress("未知");
            }else {
                String str = ipRegion.getRegion();//获取ip登录地理位置
                entity.setCityAddress(str);
            }
            userLoginLogService.insert(entity);

        });
        return Result.ok(userModel);
    }


    @PostMapping("/login/t")
    @ApiOperation("登录接口: 腾讯验证码")
    public Result loginByQQ(@Valid @RequestBody UserLoginModel loginModel, @RequestParam("ticket") String ticket,
                            @RequestParam("randstr") String tandstr, HttpServletRequest request) {
        log.info("--------> IP {}", request.getRemoteHost());
        String username = loginModel.getUsername();
        if (StringUtils.isEmpty(username)) {
            throw new ShareException(ExceptionCode.username_null_err);
        }

        if (StringUtils.isEmpty(loginModel.getPassword())) {
            throw new ShareException(ExceptionCode.password_error);
        }

        String userIp = RequestResolveUtil.getIpAddress(request);
        try {
            qqCaptchaService.verify(ticket, tandstr, userIp);
        } catch (Exception e) {
            log.debug("verify error ", e);
            throw new ShareException(ExceptionCode.code_ex);
        }

        usersService.login(username, loginModel.getPassword(), loginModel.getTelphoneCode());
        User user = usersService.findByUsername(username);
//		final User user = AccountSecurityUtils.getUser();
        UserModel userModel = getUserInfo(user);
        String header = request.getHeader("User-Agent");
        executorService.execute(() -> {
            String ip = user.getLastIp();
            UserLoginLog entity = RequestResolveUtil.resolveUserLoninLog(ip, user.getUserId(), header);
            IpRegion ipRegion =  userIpAddessMapper.getIpAddess(ip);
            if (ipRegion == null){
                entity.setCityAddress("未知区域");
            }else {
                String str = ipRegion.getRegion();//获取ip登录地理位置
                entity.setCityAddress(str);
            }
            userLoginLogService.insert(entity);
        });
        return Result.ok(userModel);
    }

    private UserModel getUserInfo(User user) {
        UserModel userModel = new UserModel();
        String lastIp = user.getLastIp();
        userModel.setLastIp(lastIp);
        Date lastTime = user.getLastTime();
        userModel.setLastTime(lastTime);
        String email = user.getUserEmail();
        userModel.setEmail(email);
        String nickName = user.getNickName();
        userModel.setNickName(nickName);
        String phone = user.getUserPhone();
        userModel.setPhone(phone);
        String photo = user.getUserPhoto();
        userModel.setPhoto(photo);
        userModel.setUserId(user.getUserId());
        userModel.setWalletAddress(userPayService.getUserWalletAddress());
        return userModel;
    }

    @GetMapping("/logout")
    @ApiOperation("登出接口")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.ok();
    }

    @PostMapping("/reg")
    @ApiOperation("注册接口-邮箱")
    @CheckCodeEmail(prefix = CODE_REG_PREFIX)
    @Transactional
    public Result reg(@Valid @RequestBody UserRegModel userReg, @RequestParam(value = "email") String email, String recommendCode, HttpServletRequest request) {
        User users = new User();

        Locale locale = RequestContextUtils.getLocale(request);
        users.setPassword(userReg.getPassword());
        users.setUserEmail(email);
        users.setUsername(userReg.getUsername());
        users.setLangType(locale.toString());
        usersService.reg(users, recommendCode);
        return Result.ok();
    }

    @PostMapping("/reg/sms")
    @ApiOperation("注册接口-短信")
    @CheckCodeSms
    @Transactional
    public Result regPhone(@Valid @RequestBody UserRegModel userReg, @RequestParam(value = "phone") String phone, String recommendCode,
                           HttpServletRequest request) {
        User users = new User();
        Locale locale = RequestContextUtils.getLocale(request);
        users.setPassword(userReg.getPassword());
        users.setUserPhone(phone);
        users.setUsername(userReg.getUsername());
        users.setLangType(locale.toString());
        usersService.reg(users, recommendCode);
        return Result.ok();
    }

    @GetMapping("/sendEmail")
    @ApiOperation("注册发送验证码")
    // 检测验证码
    //@CheckCodeCaptcha()
    public Result sendCode(@RequestParam(value = "mail") @Validated @Email String mail) {
        usersService.sendCodeByEmail(mail);
        return Result.ok();
    }

    @GetMapping("/reg/sendPhone")
    @ApiOperation("发送短信验证码-注册")
    // 检测验证码
    //@CheckCodeCaptcha()
    public Result sentRegSmsCode(
            @RequestParam("phoneNumber") @Validated @Pattern(regexp = Constant.PHONE_REG_EXP, message = "phone.error") String phoneNumber) {
        usersService.sentRegSmsCode(phoneNumber);
        return Result.ok();
    }

    @GetMapping("/resetPassword/phoneCode")
    @ApiOperation("发送短信验证码-忘记密码")
    // 检测验证码
    //@CheckCodeCaptcha()
    public Result sentRestPasswordSmsCode(
            @RequestParam("phoneNumber") @Validated @Pattern(regexp = Constant.PHONE_REG_EXP, message = "phone.error") String phoneNumber) {
        usersService.sentRestPasswordSmsCode(phoneNumber);

        return Result.ok();
    }

    @GetMapping("/resetPasswordCode")
    @ApiOperation("发送邮件验证码-忘记密码")
    // 检测验证码
    //@CheckCodeCaptcha()
    public Result resetPasswordCode(@RequestParam(value = "mail") @Validated @Email String mail) {
        usersService.resetPasswordCode(mail);

        return Result.ok();
    }

    @PostMapping("/resetPassword/phone")
    @ApiOperation(value = "忘记密码修改-短信验证")
    // 检测验证码
    @CheckCodeSms
    public Result resetPasswordCodeByPhone(@RequestBody String password,
                                           @Validated @Pattern(regexp = Constant.PHONE_REG_EXP, message = "phone.error") String phone) {
        User user = new User();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(password);
        password = new String(decode);
        user.setUserPhone(phone);
        user.setPassword(password);
        usersService.resetPassword(user);
        //记录日志
        executorService.execute(() -> {
            User users = userMapper.findByPhone(phone);
            if (user != null){
                Long userId = users.getUserId();
                LogUserOperation model = new LogUserOperation();
                model.setLogType(1);
                model.setUserType("account");
                model.setCreatedTime(new Date());
                model.setUserId(userId);
                model.setContent("用户["+users.getUsername()+"]-忘记密码（手机验证）操作成功");
                userOperationService.insert(model);
            }
        });
        return Result.ok();
    }

    /**
     * @return
     */
    @ApiOperation(value = "忘记密码修改-邮箱验证")
    @PostMapping("/resetPassword")
    @CheckCodeEmail(prefix = CODE_REST_PASSWORD_PREFIX)
    public Result resetPassword(@RequestBody String password, @Validated @Email String email) {
        User user = new User();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(password);
        password = new String(decode);
        user.setUserEmail(email);
        user.setPassword(password);
        usersService.resetPassword(user);
        //记录日志
        executorService.execute(() -> {
            User users = userMapper.findBymail(email);
            if (users != null) {
                Long userId = users.getUserId();
                LogUserOperation model = new LogUserOperation();
                model.setLogType(1);
                model.setUserType("account");
                model.setCreatedTime(new Date());
                model.setUserId(userId);
                model.setContent("用户[" + users.getUsername() + "]-忘记密码（邮箱验证）操作成功");
                userOperationService.insert(model);
            }
        });
        return Result.ok();
    }

    @GetMapping("/getCaptchaCode/{time}")
    @ApiOperation("获取验证码图片")
    public void getCaptchaCode(@PathVariable(value = "time") String time, HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", -1);
        response.setContentType("image/jpeg");
        response.setStatus(200);

//		ImageCode generateCodeImage = CodeUtil.generateCodeImage();
//		String code = generateCodeImage.getCode();
        String code = UUIDUtil.generateNumber(4);
        BufferedImage createImage = mpoolKaptcha.createImage(code);
        Serializable id = SecurityUtils.getSubject().getSession().getId();
        codeCacheService.putCode(id.toString(), code);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(createImage, "png", outputStream);
            outputStream.flush();
            log.info("write image");
        } catch (IOException e) {
            log.error("write error", e);
        }
    }

    /**
     * @param mail 邮箱 如果邮箱存在则抛出 EpoolException
     */
    @GetMapping("checkMail")
    @ApiOperation("检测邮箱是否存在")
    public Result checkExistByMail(@RequestParam(value = "mail") String mail) {
        usersService.checkExistByMail(mail);
        return Result.ok();
    }

    /**
     * @param username 如果用户名存在则抛出 EpoolException
     */
    @GetMapping("checkUsername")
    @ApiOperation("检测用户名是否存在")
    public Result checkExistByUsername(@RequestParam(value = "username") String username) {
        usersService.checkExistByUsername(username);
        return Result.ok();
    }

    /**
     * @param phone 如果手机号码存在则抛出 EpoolException
     */
    @GetMapping("checkPhone")
    @ApiOperation("检测手机号码是否存在")
    public Result checkExistByPhone(@RequestParam(value = "phone") String phone) {
        usersService.checkExistByPhone(phone);
        return Result.ok();
    }

    @GetMapping("getUserLoginModel")
    @ApiOperation("获得登录方式")
    public Result getUserLoginModel(HttpServletRequest request) {
//        SpringUtil.getActiveProfile();获取当前环境
//        获取请求头信息
        String header = request.getHeader("User-Agent");
        //获取当前登录浏览器信息
        String str = RequestResolveUtil.getBrowserInfo(header);
//        截取浏览器信息前缀
        String browser = str.toLowerCase();
        if (browser.startsWith("firefox")) {
            return Result.ok(true);
        } else {
            return Result.ok(false);
        }
    }



    @ApiOperation(value = "获取用户端登录IP保存数据库")
    @PostMapping("/ip/addess")
    public Result addIPAddess(@RequestParam("ip")String ip,@RequestParam("userId")Long userId) {

        userIpaddessService.addIpAddess(ip,userId);
        return Result.ok();
    }
}
