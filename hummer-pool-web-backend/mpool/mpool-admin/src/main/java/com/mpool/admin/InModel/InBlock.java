package com.mpool.admin.InModel;

import io.swagger.models.auth.In;

import java.io.Serializable;
import java.util.Date;
//按时间段搜索爆块记录传参
public class InBlock{

    //开始时间
    private Date strTime;
    //结束时间
    private Date endTime;

//    private Integer str;
//    private Integer end;

    public Date getStrTime() {
        return strTime;
    }

    public void setStrTime(Date strTime) {
        this.strTime = strTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
//
//    public Integer getStr() {
//        return str;
//    }
//
//    public void setStr(Integer str) {
//        this.str = str;
//    }
//
//    public Integer getEnd() {
//        return end;
//    }
//
//    public void setEnd(Integer end) {
//        this.end = end;
//    }
}
