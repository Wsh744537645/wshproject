package com.mpool.rpc.account.exception;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mpool.common.Result;
import com.mpool.common.model.account.User;
import com.mpool.rpc.account.producer.btc.MultipleShare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MultipleShareError implements MultipleShare {
    private static final Logger log = LoggerFactory.getLogger(MultipleShareError.class);
    @Override
    public Result multipleConsumer(User user, String map) {
        Gson gson = new Gson();
        JsonObject jsonElement = gson.fromJson(map, JsonObject.class);
        String value = "";
        for(String key : jsonElement.keySet()){
            value = jsonElement.get(key).toString();
            key = key.replaceAll("\"", "");
            value = value.replaceAll("\"", "");

            if(key.equals("coinType")){
                break;
            }
        }
        String error = "多币服务[" + value +"]发生故障," + " map=" + map;
        log.error("-------->>>> {}", error);
        return Result.err(error);
    }

    @Override
    public String getWorkersStatus(String ids) {
        log.error("-------->>>> 获取ZEC矿工状态信息错误");
        return null;
    }

    @Override
    public String exportWorker(User user) {
        log.error("-------->>>> 获取ZEC矿工列表错误");
        return null;
    }

    @Override
    public String getPayRecordExprot(User user) {
        log.error("-------->>>> 获取ZEC支付记录错误");
        return null;
    }

    @Override
    public String info(String id) {
        log.error("-------->>>> 访问ZEC分享数据错误");
        return null;
    }
}
