package com.mpool.account.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.mpool.account.service.FppsRatioService;
import com.mpool.account.service.WorkerService;
import com.mpool.common.model.account.fpps.FppsRatioDay;
import com.mpool.common.properties.MultipleProperties;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.constant.Constant.WorkerStatus;
import com.mpool.account.mapper.WorkerMapper;
import com.mpool.account.mapper.fpps.FppsRatioDayMapper;
import com.mpool.account.mapper.fpps.UserFppsRecordMapper;
import com.mpool.account.mapper.pool.MiningWorkersMapper;
import com.mpool.account.mapper.pool.PoolMapper;
import com.mpool.account.mapper.pool.StatsUsersDayMapper;
import com.mpool.account.mapper.pool.StatsUsersHourMapper;
import com.mpool.account.mapper.pool.StatsWorkersHourMapper;
import com.mpool.account.model.CurrentWorkerStatus;
import com.mpool.account.model.UserWorkerStatus;
import com.mpool.account.model.dashboard.SubUserStatus;
import com.mpool.account.model.dashboard.UserShareChart;
import com.mpool.account.model.dashboard.UserStatus;
import com.mpool.account.service.PoolService;
import com.mpool.account.service.bill.UserPayBillItemService;
import com.mpool.account.service.bill.UserPayService;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.bill.UserPay;
import com.mpool.common.model.account.bill.UserPayBillItem;
import com.mpool.common.model.account.fpps.UserFppsRecord;
import com.mpool.common.model.pool.MiningWorkers;
import com.mpool.common.model.pool.StatsUsersDay;
import com.mpool.common.model.pool.StatsUsersHour;
import com.mpool.common.util.DateUtil;

@Service
public class PoolServiceImpl implements PoolService {
    @Autowired
    PoolMapper poolMapper;
    @Autowired
    private MiningWorkersMapper miningWorkersMapper;
    @Autowired
    private UserPayService userPayService;
    @Autowired
    private UserPayBillItemService userPayBillItemService;
    @Autowired
    private UserFppsRecordMapper userFppsRecordMapper;
    @Autowired
    private StatsUsersHourMapper statsUsersHourMapper;
    @Autowired
    private StatsUsersDayMapper statsUsersDayMapper;
    @Autowired
    private StatsWorkersHourMapper statsWorkersHourMapper;
    @Autowired
    private FppsRatioDayMapper fppsRatioDayMapper;
    @Autowired
    private WorkerMapper workerMapper;
    @Autowired
    private FppsRatioService fppsRatioService;
    @Autowired
    private MultipleProperties multipleProperties;
    @Autowired
    private WorkerService workerService;

    @Override
    public List<Map<String, Object>> getShare30Day(String start, String end) {
        return poolMapper.getShare30Day(start, end);
    }

    private static List<String> get24Hour(Date date) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            date = DateUtil.addHour(date, -1);
            list.add(DateUtil.getYYYYMMddHH(date));
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getWorker24Online(Long userId, String start, String end) {
        List<String> hours = get24Hour(new Date());
        List<Map<String, Object>> worker24Online = poolMapper.getWorker24Online(userId, hours);
        List<String> hourskey = new ArrayList<>();
        for (Map<String, Object> map : worker24Online) {
            hourskey.add(map.get("hour").toString());
        }
        for (String hour : hours) {
            if (!hourskey.contains(hour)) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("hour", hour);
                worker24Online.add(hashMap);
            }
        }
        return worker24Online;
    }

    @Override
    public List<Map<String, Object>> getCurrentWorkerStatu(Long userId) {
        Integer allWorker = poolMapper.getAllWorkerByPuid(userId);
        if (allWorker == null) {
            allWorker = 0;
        }
        Integer offlineWorker = poolMapper.getWorkerOffline(userId);
        if (offlineWorker == null) {
            offlineWorker = 0;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        map.put("sum", allWorker - offlineWorker);
        map.put("group_id", 1);

        list.add(map);
        map = new HashMap<>();
        map.put("sum", offlineWorker);
        map.put("group_id", -1);
        list.add(map);
        return list;
    }

    @Override
    public IPage<Map<String, Object>> getFoundBlocks(IPage<Map<String, Object>> page) {
        return poolMapper.getFoundBlocks(page);
    }

    @Override
    public Map<String, Object> getUserPay(Long userId) {
        Map<String, Object> map = new HashMap<>();
        //用户钱包
        UserPay userPay = userPayService.getPayHistory(userId);
        //获得当前用户支付中的 btc总量
        String userPayBtc = userPayBillItemService.getUserPayBtc(userId);
        //用户收益记录
        UserFppsRecord fppsRecord = new UserFppsRecord();
        fppsRecord.setPuid(userId);
        //计算前一天的时间，最后转换成各式比如20190125，通过日期和userid查找结果
        Date addDay = DateUtil.addDay(new Date(), -1);
        String yyyymMdd = DateUtil.getYYYYMMdd(addDay);
        fppsRecord.setDay(Integer.parseInt(yyyymMdd));
        UserFppsRecord selectOne = userFppsRecordMapper.selectOne(new QueryWrapper<UserFppsRecord>(fppsRecord));
        long yesterday = 0;
        if (selectOne != null) {
            yesterday = selectOne.getFppsAfterFee();
        }

        // 历史交易
        map.put("totalPaid", userPay.getTotalPaid());
        //历史用过的btc
        map.put("totalDue", userPay.getTotalDue());
        // 支付中
        map.put("inPayment", userPayBtc);
        // 昨日收益
        map.put("yesterday", yesterday);
        return map;
    }

    @Override
    public Map<String, Double> getUserShareDashboard(Long userId) {
        Map<String, Double> map = new HashMap<>();
        map.put("m5", 0d);
        map.put("m15", 0d);
        map.put("h24", 0d);
        MiningWorkers miningWorkers = poolMapper.getMiningWorkersByPuid(userId);
        if (miningWorkers != null) {
            map.put("m5", map.get("m5") + miningWorkers.getHashAccept5mT());
            map.put("m15", map.get("m15") + miningWorkers.getHashAccept15mT());
        }
//		List<StatsUsersMinute> statsUserMinute5 = poolMapper.getStatsUserMinute(userId,
//				DateUtil.getYYYYMMddHHmm(m5start), DateUtil.getYYYYMMddHHmm(m5end));
//		double sum5 = statsUserMinute5.stream().map(StatsUsersMinute::getHashAcceptT).reduce(0d,
//				(sum, item) -> sum + item);
//		map.put("m5", sum5);
//		Date m10start = date;
//		Date m10end = DateUtil.addMinute(m10start, -10);
//		List<StatsUsersMinute> statsUserMinute10 = poolMapper.getStatsUserMinute(userId,
//				DateUtil.getYYYYMMddHHmm(m10start), DateUtil.getYYYYMMddHHmm(m10end));
//		double sum10 = statsUserMinute10.stream().map(StatsUsersMinute::getHashAcceptT).reduce(0d,
//				(sum, item) -> sum + item);
//		map.put("m10", String.valueOf(sum10));
        map.put("h24", getUser24HShare(userId));
        return map;
    }

    @Override
    public List<Map<String, Object>> getShare30Day(List<String> day30) {
        List<Map<String, Object>> shareInDay = poolMapper.getShareInDay(day30);
        if (shareInDay.size() == day30.size()) {
            return shareInDay;
        }
        List<String> result = new ArrayList<String>();
        for (Map<String, Object> map : shareInDay) {
            result.add(map.get("day").toString());
        }
        for (String day : day30) {
            if (result.contains(day)) {
                continue;
            } else {
                Map<String, Object> e = new HashMap<>();
                e.put("day", day);
                shareInDay.add(e);
            }
        }

        return shareInDay;
    }

    @Override
    public List<StatsUsersDay> getUser30DayShare(Long userId) {
        List<String> get30Day = DateUtil.getPast30Days();
        List<StatsUsersDay> days = poolMapper.getStatsUsersDayInDay(userId, get30Day);
        for (String day : get30Day) {
            StatsUsersDay statsUsersDay = new StatsUsersDay();
            statsUsersDay.setDay(new Long(day));
            if (!days.contains(statsUsersDay)) {
                days.add(statsUsersDay);
            }
        }
        return days;
    }

    @Override
    public List<Map<String, Object>> getUser24H(Long userId) {
        Date date = new Date();
        List<String> hours = get24Hour(date);
        String yyyymMddHH = DateUtil.getYYYYMMddHH(DateUtil.addHour(date, -25));
        hours.add(yyyymMddHH);
        List<StatsUsersHour> statsUsersHourInHour = poolMapper.getStatsUsersHourInHour(userId, hours);
        for (String hour : hours) {
            StatsUsersHour o = new StatsUsersHour();
            o.setHour(Long.parseLong(hour));
            if (!statsUsersHourInHour.contains(o)) {
                statsUsersHourInHour.add(o);
            }
        }
        List<StatsUsersHour> collect = statsUsersHourInHour.stream()
                .sorted((a, b) -> a.getHour().compareTo(b.getHour())).collect(Collectors.toList());
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 1; i < collect.size(); i++) {
            StatsUsersHour currentItem = collect.get(i);
            Map<String, Object> map = new HashMap<>();
            double hashAcceptT = currentItem.getHashAcceptT();
            double hashRejectT = currentItem.getHashRejectT();
            map.put("baseHashAcceptT", hashAcceptT);
            map.put("baseHashRejectT", hashRejectT);
            StatsUsersHour lastItem = collect.get(i - 1);
            hashAcceptT = (hashAcceptT * 0.3873) + (lastItem.getHashAcceptT() * 0.6127);
            hashRejectT = (hashRejectT * 0.3873) + (lastItem.getHashRejectT() * 0.6127);

            map.put("hour", currentItem.getHour());
            map.put("hashAcceptT", hashAcceptT);
            map.put("hashRejectT", hashRejectT);
            list.add(map);
        }
        return list;
    }

    @Override
    public MiningWorkers getMiningWorkers(Long workerId, Long userId) {
        return miningWorkersMapper.getMiningWorkers(workerId, userId);
    }

    @Override
    public List<UserStatus> getMasterRuntimeInfo(List<User> subUser) {
        Map<Long, UserStatus> result = new HashMap<>();
        List<Long> userIds = new ArrayList<>();
        for (User user : subUser) {
            UserStatus userStatus = new UserStatus();
            userStatus.setUsername(user.getUsername());
            Long userId = user.getUserId();
            userStatus.setUserId(userId);
            userIds.add(userId);
            result.put(userId, userStatus);
        }
        // 钱
        List<UserPay> userPays = userPayService.getUserPayByPuid(userIds);
        for (UserPay userPay : userPays) {
            Long puid = userPay.getPuid();
            UserStatus userStatus = result.get(puid);
            userStatus.setTotalDue(userPay.getTotalDue());
            userStatus.setTotalPaid(userPay.getTotalPaid());
        }

        List<UserPayBillItem> userPayBtc = userPayBillItemService.getUserPayInPayment(userIds);
        for (UserPayBillItem userPayBillItem : userPayBtc) {
            Long puid = userPayBillItem.getPuid();
            UserStatus userStatus = result.get(puid);
            userStatus.setInPayment(userPayBillItem.getPayBtc());
        }
        //昨日收益
        String yyyymMdd = DateUtil.getYYYYMMdd(DateUtil.addDay(new Date(), -1));
        List<UserFppsRecord> UserFppsRecords = userFppsRecordMapper.pastDayRecord(yyyymMdd, userIds);
        for (UserFppsRecord userFppsRecord : UserFppsRecords) {
            Long puid = userFppsRecord.getPuid();
            UserStatus userStatus = result.get(puid);
            userStatus.setYesterday(userFppsRecord.getFppsAfterFee());
        }

//		// 昨日已收
//		Map<Long, Long> yesterdayMap = getUsersYesterday(userIds);
//		for (Entry<Long, Long> yesterdayEntry : yesterdayMap.entrySet()) {
//			Long userId = yesterdayEntry.getKey();
//			UserStatus userStatus = result.get(userId);
//			userStatus.setYesterday(yesterdayEntry.getValue());
//		}
        // 今日预收
        Map<Long, Long> todayMap = getUsersToday(userIds);
        for (Entry<Long, Long> todayEntry : todayMap.entrySet()) {
            Long userId = todayEntry.getKey();
            UserStatus userStatus = result.get(userId);
            userStatus.setToday(todayEntry.getValue());
        }
        // 矿机状态
        // 所有矿机状态信息
        List<CurrentWorkerStatus> currentWorkerStatus = workerService.countUserWorkerStatusBatch(userIds);
        for (CurrentWorkerStatus status : currentWorkerStatus) {
            Long userId = status.getUserId();
            Integer workerStatus = status.getWorkerStatus();
            UserStatus userStatus = result.get(userId);
            if (WorkerStatus.active.getStatus().equals(workerStatus)) {
                userStatus.setWorkerActive(status.getCount());
            } else if (WorkerStatus.inactive.getStatus().equals(workerStatus)) {
                userStatus.setWorkerInactive(status.getCount());
            } else if (WorkerStatus.offline.getStatus().equals(workerStatus)) {
                userStatus.setOffLine(status.getCount());
            }
        }


        // 算力信息
        List<MiningWorkers> miningWorkersList = poolMapper.getMiningWorkersByPuidList(userIds);
        for (MiningWorkers miningWorkers : miningWorkersList) {
            Long puid = miningWorkers.getPuid();
            UserStatus userStatus = result.get(puid);
            userStatus.setShare15m(miningWorkers.getHashAccept15mT());
            userStatus.setShare5m(miningWorkers.getHashAccept5mT());
        }
        Map<Long, Double> users24hShare = getUsers24HShare(userIds);
        for (Entry<Long, Double> user24h : users24hShare.entrySet()) {
            Long puid = user24h.getKey();
            UserStatus userStatus = result.get(puid);
            userStatus.setShare24h(user24h.getValue());
        }
        return new ArrayList<>(result.values());
    }

    private Map<Long, Long> getUsersYesterday(List<Long> userIds) {
        Map<Long, Long> map = userPayBillItemService.getUsersPayYesterday(userIds);
        return map;
    }

    /**
     * 获得今日预收
     *
     * @param userIds
     * @return
     */
    private Map<Long, Long> getUsersToday(List<Long> userIds) {
        Map<Long, Long> result = new HashMap<>();
        String day = DateUtil.getYYYYMMdd(new Date());
//        FppsRatioDay ratio = fppsRatioDayMapper.getRatio(Integer.parseInt(day));
//        if (ratio == null) {
//        }else {
//            if (ratio.getNewRatio()==0.0f){
//                Float ratios =	ratio.getRatio();
//                return  ratio.getRatio();
//            }else {
//                return ratio.getNewRatio();
//            }
//        }

        Float ratio = fppsRatioService.getRatio(Integer.parseInt(day));


        List<StatsUsersDay> selectList = statsUsersDayMapper.getStatsUsersDayInPuidAndDay(userIds, day);
        for (StatsUsersDay statsUsersDay : selectList) {
            Long earn = statsUsersDay.getEarn();
            if (earn == null) {
                earn = 0L;
            }
            Long userId = statsUsersDay.getPuid();
            BigDecimal multiply = BigDecimal.valueOf(earn).multiply(BigDecimal.valueOf(ratio));
            result.put(userId, multiply.longValue());
        }

        return result;
    }

    public Map<Long, Double> getUsers24HShare(List<Long> userIds) {
        Date date = new Date();
        String bef = DateUtil.getYYYYMMddHH(date);
        Double befr = new Double(date.getMinutes()) / 60;
        Double aftr = 1 - befr;
        String aft = DateUtil.getYYYYMMddHH(DateUtil.addHour(date, -24));

        List<String> past24Hour = DateUtil.getPast24Hour();
        past24Hour.add(bef);
        List<StatsUsersHour> statsUsersHourInHourAndInPuid = statsUsersHourMapper
                .getStatsUsersHourInHourAndInPuid(past24Hour, userIds);
        Map<Long, List<StatsUsersHour>> map = new HashMap<>();
        for (Long userId : userIds) {
            map.put(userId, new ArrayList<>());
        }

        for (StatsUsersHour statsUsersHour : statsUsersHourInHourAndInPuid) {
            Long puid = statsUsersHour.getPuid();
            map.get(puid).add(statsUsersHour);
        }
        Map<Long, Double> result = new HashMap<>();
        for (Entry<Long, List<StatsUsersHour>> statsUsersHour : map.entrySet()) {
            Double value = statsUsersHour.getValue().stream().map(s -> {
                double share = 0d;
                if (s.getHour().toString().equals(bef)) {
                    share = s.getHashAcceptT() * befr;
                } else if (s.getHour().toString().equals(aft)) {
                    share = s.getHashAcceptT() * aftr;
                } else {
                    share = s.getHashAcceptT();
                }
                return share;
            }).reduce(0d, (a, b) -> a + b);
            result.put(statsUsersHour.getKey(), value / 24);
        }
        return result;
    }

    @Override
    public Map<String, List<UserShareChart>> getMasterShareChartInfoBy24(List<User> subUser) {
        List<String> past24Hour = DateUtil.getPast24Hour();

        Map<String, List<UserShareChart>> result = new HashMap<>();
        // 用于填充数据
        Map<Long, Map<Long, UserShareChart>> temp = new HashMap<>();

        List<Long> userIds = new ArrayList<>();
        // init data
        for (User user : subUser) {
            Long userId = user.getUserId();
            String username = user.getUsername();
            List<UserShareChart> userShareChartList = new ArrayList<UserShareChart>();
            result.put(username, userShareChartList);

            userIds.add(userId);

            Map<Long, UserShareChart> value = new HashMap<>();
            temp.put(userId, value);

            for (String string : past24Hour) {
                Long hour = Long.parseLong(string);
                UserShareChart userShareChart = new UserShareChart(hour);

                userShareChartList.add(userShareChart);

                value.put(hour, userShareChart);
            }
        }
        // 填充数据
        List<StatsUsersHour> statsUsersHourInHourAndInPuid = statsUsersHourMapper
                .getStatsUsersHourInHourAndInPuid(past24Hour, userIds);
        for (StatsUsersHour statsUsersHour : statsUsersHourInHourAndInPuid) {
            Long key = statsUsersHour.getPuid();
            Map<Long, UserShareChart> map = temp.get(key);
            Long hour = statsUsersHour.getHour();
            UserShareChart userShareChart = map.get(hour);
            userShareChart.setHashAcceptT(statsUsersHour.getHashAcceptT());
            userShareChart.setHashRejectT(statsUsersHour.getHashRejectT());
        }
        return result;
    }

    @Override
    public Map<String, List<UserShareChart>> getMasterShareChartInfoBy30(List<User> subUser) {
        List<String> past30Days = DateUtil.getPast30Days();
        Map<String, List<UserShareChart>> result = new HashMap<>();
        // 用于填充数据
        Map<Long, Map<Long, UserShareChart>> temp = new HashMap<>();
        //
        List<Long> userIds = new ArrayList<>();
        // init data
        for (User user : subUser) {
            Long userId = user.getUserId();
            String username = user.getUsername();
            List<UserShareChart> userShareChartList = new ArrayList<UserShareChart>();
            result.put(username, userShareChartList);

            userIds.add(userId);

            Map<Long, UserShareChart> value = new HashMap<>();
            temp.put(userId, value);

            for (String string : past30Days) {
                Long date = Long.parseLong(string);
                UserShareChart userShareChart = new UserShareChart(date);

                userShareChartList.add(userShareChart);

                value.put(date, userShareChart);
            }
        }
        // 填充数据
        List<StatsUsersDay> statsUsersDayList = statsUsersDayMapper.getStatsUsersDayInHourAndInPuid(past30Days,
                userIds);
        for (StatsUsersDay statsUsersHour : statsUsersDayList) {
            Long key = statsUsersHour.getPuid();
            Map<Long, UserShareChart> map = temp.get(key);
            Long hour = statsUsersHour.getDay();
            UserShareChart userShareChart = map.get(hour);
            userShareChart.setHashAcceptT(statsUsersHour.getHashAcceptT());
            userShareChart.setHashRejectT(statsUsersHour.getHashRejectT());
        }
        return result;
    }

    @Override
    public Map<String, List<UserWorkerStatus>> getMasterWorkerStatusBy24(List<User> subUser) {

        List<String> past24hour = DateUtil.getPast24Hour();
        Map<String, List<UserWorkerStatus>> result = new HashMap<>();
        // 用于填充数据
        Map<Long, Map<Long, UserWorkerStatus>> temp = new HashMap<>();
        //
        List<Long> userIds = new ArrayList<>();
        // init data
        for (User user : subUser) {
            Long userId = user.getUserId();
            String username = user.getUsername();
            List<UserWorkerStatus> userShareChartList = new ArrayList<UserWorkerStatus>();
            result.put(username, userShareChartList);

            userIds.add(userId);

            Map<Long, UserWorkerStatus> value = new HashMap<>();
            temp.put(userId, value);

            for (String string : past24hour) {
                Long date = Long.parseLong(string);
                UserWorkerStatus userShareChart = new UserWorkerStatus(date);

                userShareChartList.add(userShareChart);

                value.put(date, userShareChart);
            }
        }
        // 填充数据
        List<Map<String, Object>> userWorkerActive = statsWorkersHourMapper.getUserWorkerActiveByHour(past24hour,
                userIds);

        for (Map<String, Object> workerStatus : userWorkerActive) {
            Long key = Long.parseLong(workerStatus.get("puid").toString());
            Map<Long, UserWorkerStatus> map = temp.get(key);
            Long hour = Long.parseLong(workerStatus.get("hour").toString());
            UserWorkerStatus userWorkerStatus = map.get(hour);
            userWorkerStatus.setOnLine(Integer.parseInt(workerStatus.get("activeNumber").toString()));
        }
        return result;
    }

    @Override
    public SubUserStatus getSubUserRuntimeInfo(User user) {
        // 钱
        Long userId = user.getUserId();
        SubUserStatus subUserStatus = new SubUserStatus();
        UserPay userPay = userPayService.getPayHistory(userId);
        subUserStatus.setTotalDue(userPay.getTotalDue());
        subUserStatus.setTotalPaid(userPay.getTotalPaid());
        // 昨日支付
        Long yesterday = userPayBillItemService.getUserPayYesterday(userId);
        subUserStatus.setYesterday(yesterday);

        // 算力
        MiningWorkers miningWorkers = poolMapper.getMiningWorkersByPuid(userId);
        if (miningWorkers != null) {
            subUserStatus.setShare15m(miningWorkers.getHashAccept15mT());
            subUserStatus.setShare5m(miningWorkers.getHashAccept5mT());
        }
        double user24hShare = getUser24HShare(userId);
        subUserStatus.setShare24h(user24hShare);
        // 矿机状态
        Integer allWorker = poolMapper.getAllWorkerByPuid(userId);
        if (allWorker == null) {
            allWorker = 0;
        }
        //全部矿机数量
        subUserStatus.setWorkerTotal(allWorker);
        //当前在线率（活跃矿机/全部矿机数量）
//		Integer onLineRate =
//		subUserStatus.setOnlineRate();

        Integer offlineWorker = poolMapper.getWorkerOffline(userId);
        if (offlineWorker == null) {
            offlineWorker = 0;
        }
        subUserStatus.setShare15m(allWorker - offlineWorker);
        return subUserStatus;

    }

    private double getUser24HShare(Long userId) {
        Date date = new Date();
        String bef = DateUtil.getYYYYMMddHH(date);
        Double befr = new Double(date.getMinutes()) / 60;
        Double aftr = 1 - befr;
        List<String> hour24 = get24Hour(date);

        String aft = hour24.get(hour24.size() - 1);
        List<StatsUsersHour> statsUserHour = poolMapper.getStatsUsersHourInHour(userId, hour24);// (userId,
        // DateUtil.getYYYYMMddHH(h24start),
        if (statsUserHour != null && !statsUserHour.isEmpty()) {
            double sum24 = statsUserHour.stream().map(s -> {
                double share = 0d;
                if (s.getHour().toString().equals(bef)) {
                    share = s.getHashAcceptT() * befr;
                } else if (s.getHour().toString().equals(aft)) {
                    share = s.getHashAcceptT() * aftr;
                } else {
                    share = s.getHashAcceptT();
                }
                return share;
            }).reduce(0d, (sum, item) -> sum + item) / 24;
            return sum24;
        } else {
            return 0;
        }
    }
}
