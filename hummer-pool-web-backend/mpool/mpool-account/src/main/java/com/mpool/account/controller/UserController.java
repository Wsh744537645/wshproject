package com.mpool.account.controller;

import static com.mpool.account.constant.Constant.CODE_REG_PREFIX;
import static com.mpool.account.constant.Constant.CODE_REST_PASSWORD_PREFIX;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Base64.Decoder;
import java.util.concurrent.ExecutorService;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mpool.account.mapper.UserIpAddessMapper;
import com.mpool.account.mapper.UserMapper;
import com.mpool.account.service.UserIpaddessService;
import com.mpool.account.service.log.UserOperationService;
import com.mpool.account.utils.AccountSecurityUtils;
import com.mpool.common.model.account.IpRegion;
import com.mpool.common.model.log.LogUserOperation;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.mpool.account.constant.Constant;
import com.mpool.account.exception.AccountException;
import com.mpool.account.exception.ExceptionCode;
import com.mpool.account.model.MenuModel;
import com.mpool.account.model.UserLoginModel;
import com.mpool.account.model.UserModel;
import com.mpool.account.model.UserRegModel;
import com.mpool.account.service.MenuService;
import com.mpool.account.service.UserLoginLogService;
import com.mpool.account.service.UserService;
import com.mpool.account.utils.RequestResolveUtil;
import com.mpool.common.ImageCode;
import com.mpool.common.MpoolKaptcha;
import com.mpool.common.QQCaptchaService;
import com.mpool.common.Result;
import com.mpool.common.cache.aspect.annotation.CheckCodeCaptcha;
import com.mpool.common.cache.aspect.annotation.CheckCodeEmail;
import com.mpool.common.cache.aspect.annotation.CheckCodeSms;
import com.mpool.common.cache.service.CaptchaCacheService;
import com.mpool.common.model.account.Menu;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.log.UserLoginLog;
import com.mpool.common.util.CodeUtil;
import com.mpool.common.util.SpringUtil;
import com.mpool.common.util.UUIDUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author chenjian2
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
    private MenuService sysMenuService;

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

    @PostMapping("/login")
    @ApiOperation("登录接口")
    @CheckCodeCaptcha()
    public Result login(@Valid @RequestBody UserLoginModel loginModel, HttpServletRequest request) {
        log.info("--------> IP {}", request.getRemoteHost());
        String username = loginModel.getUsername();
        if (StringUtils.isEmpty(username)) {
            throw new AccountException(ExceptionCode.username_null_err);
        }

        usersService.login(username, loginModel.getPassword(), loginModel.getTelphoneCode());
        User user = usersService.findByUsername(username);
//		final User user = AccountSecurityUtils.getUser();
        UserModel userModel = getUserInfo(user);
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
            throw new AccountException(ExceptionCode.username_null_err);
        }

        if (StringUtils.isEmpty(loginModel.getPassword())) {
            throw new AccountException(ExceptionCode.password_error);
        }

        String userIp = RequestResolveUtil.getIpAddress(request);
        try {
            qqCaptchaService.verify(ticket, tandstr, userIp);
        } catch (Exception e) {
            log.debug("verify error ", e);
            throw new AccountException(ExceptionCode.code_ex);
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
        String sex = user.getUserSex();
        userModel.setSex(sex);
        userModel.setMasterUserId(user.getMasterUserId());
        return userModel;
    }

    @GetMapping("/logout")
    @ApiOperation("登出接口")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.ok();
    }

    @GetMapping("/menus")
    @ApiOperation("获取菜单和语言")
    public Result getMenus() {
        User user = (User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        List<Menu> sysMenus = sysMenuService.findByUserId(user.getUserId());
        if (sysMenus == null || sysMenus.size() == 0) {
            log.info("Insufficient user privileges or account errors userId=>{}", user.getUserId());
            return Result.err();
        }
        Set<MenuModel> menus = sysMenuService.transformMenuModel(sysMenus);
//		Map<String, Object> userLang = sysLangService.getUserLang(user.getLangType());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("menu", menus);
//		result.put("lang", userLang);
        return Result.ok(result);
    }

    @PostMapping("/reg")
    @ApiOperation("注册接口-邮箱")
    @CheckCodeEmail(prefix = CODE_REG_PREFIX)
    @Transactional
    public Result reg(@Valid @RequestBody UserRegModel userReg, @RequestParam(value = "email") String email,
                      HttpServletRequest request) {
        User users = new User();

        Locale locale = RequestContextUtils.getLocale(request);
        users.setPassword(userReg.getPassword());
        users.setUserEmail(email);
        users.setUsername(userReg.getUsername());
        users.setLangType(locale.toString());
        usersService.reg(users);
        return Result.ok();
    }

    @PostMapping("/reg/sms")
    @ApiOperation("注册接口-短信")
    @CheckCodeSms
    @Transactional
    public Result regPhone(@Valid @RequestBody UserRegModel userReg, @RequestParam(value = "phone") String phone,
                           HttpServletRequest request) {
        User users = new User();
        Locale locale = RequestContextUtils.getLocale(request);
        users.setPassword(userReg.getPassword());
        users.setUserPhone(phone);
        users.setUsername(userReg.getUsername());
        users.setLangType(locale.toString());
        usersService.reg(users);
        return Result.ok();
    }

    @GetMapping("/sendEmail")
    @ApiOperation("注册发送验证码")
    // 检测验证码
    //@CheckCodeCaptcha()
    public Result sendCode(@RequestParam(value = "mail") @Validated @Email String mail) {
        String result = usersService.sendCodeByEmail(mail);
        return Result.ok(result);
    }

    @GetMapping("/reg/sendPhone")
    @ApiOperation("发送短信验证码-注册")
    // 检测验证码
    //@CheckCodeCaptcha()
    public Result sentRegSmsCode(
            @RequestParam("phoneNumber") @Validated @Pattern(regexp = Constant.PHONE_REG_EXP, message = "phone.error") String phoneNumber) {
        String result = usersService.sentRegSmsCode(phoneNumber);
        return Result.ok(result);
    }

    @GetMapping("/resetPassword/phoneCode")
    @ApiOperation("发送短信验证码-忘记密码")
    // 检测验证码
    //@CheckCodeCaptcha()
    public Result sentRestPasswordSmsCode(
            @RequestParam("phoneNumber") @Validated @Pattern(regexp = Constant.PHONE_REG_EXP, message = "phone.error") String phoneNumber) {
        String result = usersService.sentRestPasswordSmsCode(phoneNumber);

        return Result.ok(result);
    }

    @GetMapping("/resetPasswordCode")
    @ApiOperation("发送邮件验证码-忘记密码")
    // 检测验证码
    //@CheckCodeCaptcha()
    public Result resetPasswordCode(@RequestParam(value = "mail") @Validated @Email String mail) {
        String result = usersService.resetPasswordCode(mail);

        return Result.ok(result);
    }

    @PostMapping("/resetPassword/phone")
    @ApiOperation(value = "忘记密码修改-短信验证")
    // 检测验证码
    @CheckCodeSms
    public Result resetPasswordCodeByPhone(@RequestBody String password,
                                           @Validated @Pattern(regexp = Constant.PHONE_REG_EXP, message = "phone.error") String phone) {
        User user = new User();
        Decoder decoder = Base64.getDecoder();
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
     * @param type
     * @param account
     * @return
     */
    @ApiOperation(value = "忘记密码修改-邮箱验证")
    @PostMapping("/resetPassword")
    @CheckCodeEmail(prefix = CODE_REST_PASSWORD_PREFIX)
    public Result resetPassword(@RequestBody String password, @Validated @Email String email) {
        User user = new User();
        Decoder decoder = Base64.getDecoder();
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
