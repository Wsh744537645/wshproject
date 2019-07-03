package com.mpool.share.controller;

import com.mpool.common.Result;
import com.mpool.share.service.RecommendService;
import com.mpool.share.utils.AccountSecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/6/4 16:52
 */

@RestController
@RequestMapping({"/recommend", "apis/recommend"})
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    @GetMapping("/normal")
    @ApiOperation("获取用户邀请面板基本信息")
    public Result getNormal(){
        Long userId = AccountSecurityUtils.getUser().getUserId();
        return Result.ok(recommendService.getNormalInfo(userId));
    }

    @GetMapping("/invited/list")
    @ApiOperation("获取邀请人列表")
    public Result getInvitedList(){
        Long userId = AccountSecurityUtils.getUser().getUserId();
        return Result.ok(recommendService.getInviteUserList(userId));
    }

    @GetMapping("/share/list")
    @ApiOperation("获取返佣详情列表")
    public Result getInvitedShareList(){
        Long userId = AccountSecurityUtils.getUser().getUserId();
        List<Map<String, Object>> invitedMap = recommendService.getInviteUserList(userId);
        List<Map<String, Object>> shareMap = recommendService.getInviteShareList(userId);

        for(Map<String, Object> map : invitedMap){
            String uid = map.get("uid").toString();
            for(Map<String, Object> tmp : shareMap){
                String tmp_uid = tmp.get("uid").toString();
                if(uid.equals(tmp_uid)){
                    map.put("earn", tmp.get("earn"));
                    break;
                }
            }
        }

        return Result.ok(invitedMap);
    }
}
