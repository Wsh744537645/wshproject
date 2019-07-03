package com.mpool.accountmultiple.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.accountmultiple.mapper.PoolNodeMapper;
import com.mpool.accountmultiple.mapper.UserMapper;
import com.mpool.accountmultiple.utils.AccountUtils;
import com.mpool.accountmultiple.utils.ParseRequestUtils;
import com.mpool.mpoolaccountmultiplecommon.model.PoolNodeModel;
import com.mpool.mpoolaccountmultiplecommon.model.UserWorkerStatus;
import com.mpool.mpoolaccountmultiplecommon.model.dashboard.UserShareChart;
import com.mpool.mpoolaccountmultiplecommon.model.dashboard.UserStatus;
import com.mpool.accountmultiple.service.PoolService;
import com.mpool.accountmultiple.service.UserService;
import com.mpool.accountmultiple.service.bill.UserPayService;
import com.mpool.common.Result;
import com.mpool.common.model.account.User;
import com.mpool.common.model.pool.StatsUsersDay;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@RestController
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
    private PoolNodeMapper poolNodeMapper;

    @RequestMapping("/getUser24H")
    @ApiOperation("获取用户24小时 算力")
    //@RequiresPermissions("son")
    public Result getUser24H() {
        Long userId = AccountUtils.getCurrentUserId();

        List<Map<String, Object>> hour24 = poolService.getUser24H(userId);
        return Result.ok(hour24);

        // 获取当前登录用户getMasterRuntimeInfo
//        User user = AccountSecurityUtils.getUser();
//        Long userId = user.getUserId();
//        List<Map<String, Object>> hour24 = null;
//        hour24 = poolService.getUser24H(userId);
//        return Result.ok(hour24);
    }

    @RequestMapping("/getWorker24Online")
    @ApiOperation("获取24小时在线矿机")
    //@RequiresPermissions("son")
    public Result getWorker24Online() {
        Long userId = AccountUtils.getCurrentUserId();

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

    @RequestMapping("/getCurrentWorkerStatu")
    @ApiOperation("获得当前矿机在线")
    //@RequiresPermissions("son")
    public Result getCurrentWorkerStatu() {
        // 获取当前登录用户
        User user = AccountUtils.getUser();
        List<Map<String, Object>> data = null;
        data = poolService.getCurrentWorkerStatu(user.getUserId());
        return Result.ok(data);
    }

    @RequestMapping("/getUser30Day")
    @ApiOperation("获取用户30天 算力")
    //@RequiresPermissions("son")
    public Result getUser30Day() {
        Long userId = AccountUtils.getCurrentUserId();

        List<StatsUsersDay> share30 = poolService.getUser30DayShare(userId);
        return Result.ok(share30);
    }

    @RequestMapping("/getUserPay")
    @ApiOperation("获取用户钱包")
    //@RequiresPermissions("son")
    public Result getUserPay() {
        User user = AccountUtils.getUser();
        Long userId = user.getUserId();
        Map<String, Object> pay = poolService.getUserPay(userId);
        return Result.ok(pay);
    }

    @RequestMapping("/getCurrentBtc")
    @ApiOperation("获得用户当天收益btc")
    //@RequiresPermissions("son")
    public Result getCurrentBtc() {
        User user = AccountUtils.getUser();
        Long userId = user.getUserId();
        BigDecimal payCurrent = userPayService.getPayCurrent(userId);
        return Result.ok(payCurrent);
    }

    @RequestMapping("/getUserShare")
    @ApiOperation("获得5分钟算力10分钟算力24小时算力")
    //@RequiresPermissions("son")
    public Result getUserShare() {
        User user = AccountUtils.getUser();
        Map<String, Double> map = null;
        map = poolService.getUserShareDashboard(user.getUserId());
        return Result.ok(map);
    }

    @RequestMapping("/getMasterShareChartInfo/24")
    @ApiOperation("主账号获得所有子账号算力图表24小时")
    //@RequiresPermissions("master")
    public Result getMasterShareChartInfo() {
        Long userId = AccountUtils.getUser().getUserId();
        List<User> subUser = userService.findSubAccountByCurrencyId(userId);
        if (subUser.size() == 0) {
            return Result.ok();
        }
        Map<String, List<UserShareChart>> result = poolService.getMasterShareChartInfoBy24(subUser);
        return Result.ok(result);
    }

    @RequestMapping("/getMasterWorkerInfo/24")
    @ApiOperation("主账号获得所有子账号24小时活跃数")
    //@RequiresPermissions("master")
    public Result getMasterWorkerStatusBy30() {
        Long userId = AccountUtils.getUser().getUserId();
        List<User> subUser = userService.findSubAccountByCurrencyId(userId);
        if (subUser.size() == 0) {
            return Result.ok();
        }
        Map<String, List<UserWorkerStatus>> result = poolService.getMasterWorkerStatusBy24(subUser);
        return Result.ok(result);
    }

    @RequestMapping("/getMasterShareChartInfo/30")
    @ApiOperation("主账号获得所有子账号算力图表30天")
    //@RequiresPermissions("master")
    public Result getMasterShareChartInfoBy30() {
        Long userId = AccountUtils.getUser().getUserId();
        List<User> subUser = userService.findSubAccountByCurrencyId(userId);
        if (subUser.size() == 0) {
            return Result.ok();
        }
        Map<String, List<UserShareChart>> result = poolService.getMasterShareChartInfoBy30(subUser);
        return Result.ok(result);
    }

    @RequestMapping("/getMasterRuntimeInfo")
    @ApiOperation("主账号获得所有子账号运行时数据")
    //@RequiresPermissions("master")
    public Result getMasterRuntimeInfo() {
        PageModel model = ParseRequestUtils.getObjectValue(PageModel.class);

        Long userId = AccountUtils.getUser().getUserId();
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

    @RequestMapping("/getMasterRuntimeInfo/converge")
    @ApiOperation("主账号获得所有子账号运行时数据总和")
    //@RequiresPermissions("master")
    public Result getMasterRuntimeInfoConverge() {
        //TODO 还需要区分币种

        Long userId = AccountUtils.getCurrentUserId();

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
        userStatus.setUsername(AccountUtils.getUser().getUsername());
        return Result.ok(userStatus);
    }

    @RequestMapping("/getSubRuntimeInfo")
    @ApiOperation("子账号获得自己的运行时数据")
    //@RequiresPermissions("son")
    public Result getSubRuntimeInfo() {
        Long userId = ParseRequestUtils.getLongValueByKey("userId");

        User user = null;
        if (userId == null) {
            HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest();
            Long shareAddUserId = (Long) request.getAttribute("shareAddUserId");
            if(shareAddUserId == null){
                // 获取当前登录用户
                user = AccountUtils.getUser();
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
    public Result getPoolNodes(){
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
