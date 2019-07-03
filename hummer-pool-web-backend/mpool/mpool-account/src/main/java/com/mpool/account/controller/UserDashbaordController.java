package com.mpool.account.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mpool.account.mapper.PoolNodeMapper;
import com.mpool.account.mapper.UserMapper;
import com.mpool.account.service.CurrencyService;
import com.mpool.common.model.account.Currency;
import com.mpool.common.properties.MultipleProperties;
import com.mpool.mpoolaccountmultiplecommon.model.PoolNodeModel;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.model.UserWorkerStatus;
import com.mpool.account.model.dashboard.UserShareChart;
import com.mpool.account.model.dashboard.UserStatus;
import com.mpool.account.service.PoolService;
import com.mpool.account.service.UserRegionSerice;
import com.mpool.account.service.UserService;
import com.mpool.account.service.bill.UserPayService;
import com.mpool.account.utils.AccountSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.account.User;
import com.mpool.common.model.pool.StatsUsersDay;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = {"/apis/user/dashbaord", "/user/dashbaord"})
@Api("用户面板")
public class UserDashbaordController {
    @Autowired
    private PoolService poolService;
    @Autowired
    private UserPayService userPayService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MultipleProperties multipleProperties;
    @Autowired
    private PoolNodeMapper poolNodeMapper;

    @GetMapping("/getUser24H")
    @ApiOperation("获取用户24小时 算力")
    //@RequiresPermissions("son")
    public String getUser24H(){
        String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
        if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
            return "forward:/user/dashbaord/getUser24Hbtc";
        }
        else{

            return "forward:/mutiple/" + currencyName + "?url=user/dashbaord/getUser24H";
        }
    }

    @GetMapping("/getUser24Hbtc")
    @ApiOperation("获取用户24小时 算力")
    @ResponseBody
    //@RequiresPermissions("son")
    public Result getUser24HBtc() {
        Long userId = AccountSecurityUtils.getCurrentUserId();

        List<Map<String, Object>> hour24 = poolService.getUser24H(userId);
        return Result.ok(hour24);

        // 获取当前登录用户
//        User user = AccountSecurityUtils.getUser();
//        Long userId = user.getUserId();
//        List<Map<String, Object>> hour24 = null;
//        hour24 = poolService.getUser24H(userId);
//        return Result.ok(hour24);
    }

    @GetMapping("/getWorker24Online")
    @ApiOperation("获取24小时在线矿机")
    //@RequiresPermissions("son")
    public String getWorker24Online(){
        String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
        if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
            return "forward:/user/dashbaord/getWorker24Onlinebtc";
        }
        else{

            return "forward:/mutiple/" + currencyName + "?url=user/dashbaord/getWorker24Online";
        }
    }

    @GetMapping("/getWorker24Onlinebtc")
    @ApiOperation("获取24小时在线矿机")
    //@RequiresPermissions("son")
    @ResponseBody
    public Result getWorker24OnlineBtc() {
        Long userId = AccountSecurityUtils.getCurrentUserId();

        Date date = new Date();
        date = DateUtil.addHour(date, -1);
        String end = DateUtil.getYYYYMMddHH(date);
        date = DateUtil.addHour(date, -24);
        String start = DateUtil.getYYYYMMddHH(date);
        List<Map<String, Object>> data = null;
        data = poolService.getWorker24Online(userId, start, end);
        return Result.ok(data);


//        // 获取当前登录用户
//        User user = AccountSecurityUtils.getUser();
//        Date date = new Date();
//        date = DateUtil.addHour(date, -1);
//        String end = DateUtil.getYYYYMMddHH(date);
//        date = DateUtil.addHour(date, -24);
//        String start = DateUtil.getYYYYMMddHH(date);
//        Long userId = user.getUserId();
//        List<Map<String, Object>> data = null;
//        data = poolService.getWorker24Online(userId, start, end);
//        return Result.ok(data);
    }

    @GetMapping("/getCurrentWorkerStatu")
    @ApiOperation("获得当前矿机在线")
    @RequiresPermissions("son")
    @ResponseBody
    public Result getCurrentWorkerStatu() {
        // 获取当前登录用户
        User user = AccountSecurityUtils.getUser();
        List<Map<String, Object>> data = null;
        data = poolService.getCurrentWorkerStatu(user.getUserId());
        return Result.ok(data);
    }

    @GetMapping("/getUser30Day")
    @ApiOperation("获取用户30天 算力")
    //@RequiresPermissions("son")
    public String getUser30Day(){
        String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
        if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
            return "forward:/user/dashbaord/getUser30Daybtc";
        }
        else{

            return "forward:/mutiple/" + currencyName + "?url=user/dashbaord/getUser30Day";
        }
    }

    @GetMapping("/getUser30Daybtc")
    @ApiOperation("获取用户30天 算力")
    //@RequiresPermissions("son")
    @ResponseBody
    public Result getUser30DayBtc() {
        Long userId = AccountSecurityUtils.getCurrentUserId();

        List<StatsUsersDay> share30 = poolService.getUser30DayShare(userId);
        return Result.ok(share30);
    }

    @GetMapping("/getUserPay")
    @ApiOperation("获取用户钱包")
    @RequiresPermissions("son")
    @ResponseBody
    public Result getUserPay() {
        User user = AccountSecurityUtils.getUser();
        Long userId = user.getUserId();
        Map<String, Object> pay = poolService.getUserPay(userId);
        return Result.ok(pay);
    }

    @GetMapping("/getCurrentBtc")
    @ApiOperation("获得用户当天收益btc")
    @RequiresPermissions("son")
    @ResponseBody
    public Result getCurrentBtc() {
        User user = AccountSecurityUtils.getUser();
        Long userId = user.getUserId();
        BigDecimal payCurrent = userPayService.getPayCurrent(userId);
        return Result.ok(payCurrent);
    }

    @GetMapping("/getUserShare")
    @ApiOperation("获得5分钟算力10分钟算力24小时算力")
    @RequiresPermissions("son")
    @ResponseBody
    public Result getUserShare() {
        User user = AccountSecurityUtils.getUser();
        Map<String, Double> map = null;
        map = poolService.getUserShareDashboard(user.getUserId());
        return Result.ok(map);
    }

    @GetMapping("/getMasterShareChartInfo/24")
    @ApiOperation("主账号获得所有子账号算力图表24小时")
    @RequiresPermissions("master")
    public String getMasterShareChartInfo(String currencyName) {
        if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
            return "forward:/user/dashbaord/getMasterShareChartInfobtc/24";
        }
        else{

            return "forward:/mutiple/" + currencyName + "?url=user/dashbaord/getMasterShareChartInfo/24";
        }
    }

    @GetMapping("/getMasterShareChartInfobtc/24")
    @ResponseBody
    public Result getMasterShareChartInfoBtc() {
        Long userId = AccountSecurityUtils.getUser().getUserId();
        List<User> subUser = userService.findSubAccountByCurrencyId(userId);
        if (subUser.size() == 0) {
            return Result.ok();
        }
        Map<String, List<UserShareChart>> result = poolService.getMasterShareChartInfoBy24(subUser);
        return Result.ok(result);
    }

    @GetMapping("/getMasterWorkerInfo/24")
    @ApiOperation("主账号获得所有子账号24小时活跃数")
    @RequiresPermissions("master")
    public String getMasterWorkerStatusBy30(String currencyName) {
        if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
            return "forward:/user/dashbaord/getMasterWorkerInfobtc/24";
        }
        else{

            return "forward:/mutiple/" + currencyName + "?url=user/dashbaord/getMasterWorkerInfo/24";
        }
    }

    @GetMapping("/getMasterWorkerInfobtc/24")
    @ResponseBody
    public Result getMasterWorkerStatusBy30Btc() {
        Long userId = AccountSecurityUtils.getUser().getUserId();
        List<User> subUser = userService.findSubAccountByCurrencyId(userId);
        if (subUser.size() == 0) {
            return Result.ok();
        }
        Map<String, List<UserWorkerStatus>> result = poolService.getMasterWorkerStatusBy24(subUser);
        return Result.ok(result);
    }

    @GetMapping("/getMasterShareChartInfo/30")
    @ApiOperation("主账号获得所有子账号算力图表30天")
    @RequiresPermissions("master")
    public String getMasterShareChartInfoBy30(String currencyName) {
        if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
            return "forward:/user/dashbaord/getMasterShareChartInfobtc/30";
        }
        else{

            return "forward:/mutiple/" + currencyName + "?url=user/dashbaord/getMasterShareChartInfo/30";
        }
    }

    @GetMapping("/getMasterShareChartInfobtc/30")
    @ResponseBody
    public Result getMasterShareChartInfoBy30Btc() {
        Long userId = AccountSecurityUtils.getUser().getUserId();
        List<User> subUser = userService.findSubAccountByCurrencyId(userId);
        if (subUser.size() == 0) {
            return Result.ok();
        }
        Map<String, List<UserShareChart>> result = poolService.getMasterShareChartInfoBy30(subUser);
        return Result.ok(result);
    }

    @GetMapping("/getMasterRuntimeInfo")
    @ApiOperation("主账号获得所有子账号运行时数据")
    @RequiresPermissions("master")
    public String getMasterRuntimeInfo(PageModel model, String currencyName) {
        if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
            return "forward:/user/dashbaord/getMasterRuntimeInfobtc";
        }
        else{

            return "forward:/mutiple/" + currencyName + "?url=user/dashbaord/getMasterRuntimeInfo";
        }
    }

    @GetMapping("/getMasterRuntimeInfobtc")
    @ResponseBody
    public Result getMasterRuntimeInfoBtc(PageModel model) {
        Long userId = AccountSecurityUtils.getUser().getUserId();
        //		微信测试 Long userId = 4L;
        IPage<User> iPage = new Page<User>(model);

        User user = new User();
        user.setMasterUserId(userId);
        //iPage = userService.selectUserPage(iPage, user);
        iPage = userService.selectUserPageByPayCurrencyId(iPage, userId);
        // List<User> subUser = userService.findSubAccount(userId + "");
        if (iPage.getRecords().size() == 0) {
            return Result.ok();
        }
        List<UserStatus> list = poolService.getMasterRuntimeInfo(iPage.getRecords());
        Page<UserStatus> page = new Page<>(iPage);
        page.setRecords(list);
        return Result.ok(page);
    }

    @GetMapping("/getMasterRuntimeInfo/converge")
    @ApiOperation("主账号获得所有子账号运行时数据总和")
    public String getMasterRuntimeInfoConverge(String currencyName){
        if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
            return "forward:/user/dashbaord/getMasterRuntimeInfobtc/converge";
        }
        else{

            return "forward:/mutiple/" + currencyName + "?url=user/dashbaord/getMasterRuntimeInfo/converge";
        }
    }

    @GetMapping("/getMasterRuntimeInfobtc/converge")
    @ApiOperation("主账号获得所有子账号运行时数据总和")
    //@RequiresPermissions("master")
    @ResponseBody
    public Result getMasterRuntimeInfoConvergeBtc() {
        Long userId = AccountSecurityUtils.getCurrentUserId();

        List<User> subUser = userService.findSubAccount(userId + "");
        UserStatus userStatus = new UserStatus();
        if (subUser.size() == 0) {
            return Result.ok(userStatus);
        }
        List<UserStatus> list = poolService.getMasterRuntimeInfo(subUser);
        //获取主账户（登录账户）用户名
        for (UserStatus item : list) {
            userStatus.add(item);
        }
        userStatus.setUsername(AccountSecurityUtils.getUser().getUsername());
        return Result.ok(userStatus);
    }

    @GetMapping("/getSubRuntimeInfo")
    @ApiOperation("子账号获得自己的运行时数据")
    //@RequiresPermissions("son")
    public String getSubRuntimeInfo(@RequestParam(value = "userId", required = false) Long userId){
        String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
        if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
            return "forward:/user/dashbaord/getSubRuntimeInfobtc";
        }
        else{

            return "forward:/mutiple/" + currencyName + "?url=user/dashbaord/getSubRuntimeInfo";
        }
    }

    @GetMapping("/getSubRuntimeInfobtc")
    @ApiOperation("子账号获得自己的运行时数据")
    //@RequiresPermissions("son")
    @ResponseBody
    public Result getSubRuntimeInfoBtc(@RequestParam(value = "userId", required = false) Long userId) {
        User user = null;
        if (userId == null) {
            HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest();
            Long shareAddUserId = (Long) request.getAttribute("shareAddUserId");
            if(shareAddUserId == null){
                // 获取当前登录用户
                user = AccountSecurityUtils.getUser();
            }else{
                user = new User();
                user.setUserId(shareAddUserId);
            }

        } else {
            user = new User();
            user.setUserId(userId);
        }
        List<User> list = new ArrayList<>();
        list.add(user);
        UserStatus userStatus = poolService.getMasterRuntimeInfo(list).get(0);
        if (userStatus == null) {
            userStatus = new UserStatus();
        }
        return Result.ok(userStatus);
    }

    @RequestMapping("/getPoolNodes")
    public String getPoolNodes(String currencyName){
        if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
            return "forward:/user/dashbaord/getPoolNodesbtc";
        }
        else{

            return "forward:/mutiple/" + currencyName + "?url=user/dashbaord/getPoolNodes";
        }
    }

    @RequestMapping("/getPoolNodesbtc")
    @ResponseBody
    public Result getPoolNodesBtc(){
        List<PoolNodeModel> list = poolNodeMapper.getAllPoolNode();
        Map<String, List<PoolNodeModel>> result = new HashMap<>();
        for(PoolNodeModel model : list){
            if(!result.containsKey(model.getRegion())){
                List<PoolNodeModel> poolNodeModels = new ArrayList<>();
                result.put(model.getRegion(), poolNodeModels);
            }
            result.get(model.getRegion()).add(model);
        }
        return Result.ok(result);
    }
}
