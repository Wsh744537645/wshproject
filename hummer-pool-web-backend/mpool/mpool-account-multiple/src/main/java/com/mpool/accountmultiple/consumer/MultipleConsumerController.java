package com.mpool.accountmultiple.consumer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mpool.accountmultiple.mapper.WorkerMapper;
import com.mpool.accountmultiple.mapper.pool.PoolMapper;
import com.mpool.accountmultiple.service.WorkerService;
import com.mpool.accountmultiple.utils.AccountUtils;
import com.mpool.common.model.account.User;
import com.mpool.common.model.pool.MiningWorkers;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.GsonUtil;
import com.mpool.mpoolaccountmultiplecommon.model.CurrentWorkerStatus;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/multiple")
public class MultipleConsumerController {
    private final static Logger log= LoggerFactory.getLogger(MultipleConsumerController.class);

    @Autowired
    private PoolMapper poolMapper;
    @Autowired
    private WorkerService workerService;

    @RequestMapping("/consumer")
    public String multipleConsumer(@RequestBody User user, @RequestParam(value = "map") String map, HttpServletRequest request){
        AccountUtils.setUser(user);

        String url = "";
        String coinType = "";
        Gson gson = GsonUtil.getGson();
        JsonObject jsonElement = gson.fromJson(map, JsonObject.class);
        for(String key : jsonElement.keySet()){
            String value = jsonElement.get(key).toString();
            key = key.replaceAll("\"", "");
            value = value.replaceAll("\"", "");

            if(key.equals("url")){
                url = value;
            }else if(key.equals("curSessionId")){
                AccountUtils.setSessionID(value);
                continue;
            }else if(key.equals("coinType")){
                coinType = value;
            }

            request.setAttribute(key, value);
        }

        if(!url.isEmpty()){
            return "forward:/" + url;
        }

        log.error("multiple[{}] consumer>>>>> url is null, map = {}", coinType, map);
        return "url is null";
    }

    /**
     * 获得矿工信息
     * @param ids
     * @return
     */
    @RequestMapping("/getWorkersStatus")
    @ResponseBody
    public String getWorkersStatus(@RequestParam(value = "ids") String ids){
        Gson gson = GsonUtil.getGson();
        List<Long> userIds = gson.fromJson(ids, List.class);

        Map<String, Object> result = new HashMap<>();
        List<MiningWorkers> miningWorkersList = poolMapper.getMiningWorkersByPuidList(userIds);

        List<CurrentWorkerStatus> countUserWorkerStatusBatch = workerService.countUserWorkerStatusBatch(userIds);

        result.put("MiningWorkers", miningWorkersList);
        result.put("WorkerStatus", countUserWorkerStatusBatch);
        return gson.toJson(result);
    }
}
