package com.mpool.rpc.account.producer.btc;

import com.mpool.common.Result;
import com.mpool.common.model.account.User;
import com.mpool.rpc.UrlConfig;
import com.mpool.rpc.account.exception.MultipleShareError;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@FeignClient(name = UrlConfig.MULTIPLE_SERVICE_NAME_ZCRASH, url = UrlConfig.MULTIPLE_SERVICE_ZCRASH, fallback = MultipleShareError.class)
public interface MultipleShare {

    @RequestMapping("/multiple/consumer")
    Result multipleConsumer(@RequestBody User user, @RequestParam(value = "map") String map);

    /**
     * 获得矿工信息
     * @param ids
     * @return
     */
    @RequestMapping("/multiple/getWorkersStatus")
    String getWorkersStatus(@RequestParam(value = "ids") String ids);

    /**
     * 导出矿工列表
     * @return
     */
    @RequestMapping(value = "/worker/workerList/export")
    String exportWorker(@RequestBody User user);

    /**
     * 导出支付记录
     * @return
     */
    @RequestMapping("/wallet/pay/record/export")
    String getPayRecordExprot(@RequestBody User user);

    /**
     * 获得分享页面信息
     * @param id
     * @return
     */
    @RequestMapping("/share/{id}")
    String info(@PathVariable(value = "id") String id);
}
