package com.mpool.common.model.account;

import com.mpool.common.model.pool.utils.MathShare;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/18 17:07
 */
public class WorkerVo extends Worker {

    private static final int M5 = 60 * 5;
    private static final int M15 = 60 * 15;

    @Override
    public Double getHashAccept5mT() {
        return MathShare.mathShareDouble(Double2Long(hashAccept5mT), M5);
    }

    public Double getHashAccept15mT() {
        return MathShare.mathShareDouble(Double2Long(hashAccept15mT), M15);
    }

    @Override
    public Double getHashReject15mT() {
        return MathShare.mathShareDouble(Double2Long(hashReject15mT), M15);
    }

    public Double getHashAccept1hT() {
        return MathShare.MathShareHourDouble(Double2Long(hashAccept1hT));
    }

    public Double getHashReject1hT() {
        return MathShare.MathShareHourDouble(Double2Long(hashReject1hT));
    }

    public Double getRateReject15m() {
        double d = getHashAccept15mT() + getHashReject15mT();
        if (d != 0d) {
            double doubleValue = BigDecimal.valueOf(getHashReject15mT() / d)
                    .setScale(3, BigDecimal.ROUND_FLOOR).doubleValue();
            setRateReject15m(doubleValue);
        } else {
            setRateReject15m(0d);
        }
        return rateReject15m;
    }

    public Double getRateReject1h() {
        double e = getHashAccept1hT() + getHashReject1hT();
        if (e != 0d) {
            double doubleValue = BigDecimal.valueOf(getHashReject1hT() / e)
                    .setScale(3, BigDecimal.ROUND_FLOOR).doubleValue();
            setRateReject1h(doubleValue);
        } else {
            setRateReject1h(0d);
        }
        return rateReject1h;
    }

    private Long Double2Long(Double d){
        return (long)d.doubleValue();
    }

    public WorkerVo(Map<String, Object> map){
        if(map.containsKey("workerId")) {
            workerId = Long.parseLong(map.get("workerId").toString());
        }else{
            workerId = 0L;
        }

        if(map.containsKey("userId")) {
            userId = Long.parseLong(map.get("userId").toString());
        }else{
            userId = 0L;
        }

        if(map.containsKey("workerName")) {
            workerName = map.get("workerName").toString();
        }

        if(map.containsKey("hashAccept5mT")) {
            hashAccept5mT = Double.parseDouble(map.get("hashAccept5mT").toString());
        }else{
            hashAccept5mT = 0d;
        }

        if(map.containsKey("hashAccept15mT")) {
            hashAccept15mT = Double.parseDouble(map.get("hashAccept15mT").toString());
        }else{
            hashAccept15mT = 0d;
        }

        if(map.containsKey("hashReject15mT")) {
            hashReject15mT = Double.parseDouble(map.get("hashReject15mT").toString());
        }else{
            hashReject15mT = 0d;
        }

        if(map.containsKey("hashAccept1hT")) {
            hashAccept1hT = Double.parseDouble(map.get("hashAccept1hT").toString());
        }else{
            hashAccept1hT = 0d;
        }

        if(map.containsKey("hashReject1hT")) {
            hashReject1hT = Double.parseDouble(map.get("hashReject1hT").toString());
        }else{
            hashReject1hT = 0d;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if(map.containsKey("lastShareTime")){
                lastShareTime = sdf.parse(map.get("lastShareTime").toString());
            }else{
                lastShareTime = new Date();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        updateStatus();
    }

    public void resetMap(Map<String, Object> map){
        map.put("hashAccept5mT", getHashAccept5mT());
        map.put("hashAccept15mT", getHashAccept15mT());
        map.put("hashReject15mT", getHashReject15mT());
        map.put("hashAccept1hT", getHashAccept1hT());
        map.put("hashReject1hT", getRateReject1h());
        map.put("regionId", getRegionId());
        map.put("workerStatus", getWorkerStatus());
    }
}
