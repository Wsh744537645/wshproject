package com.mpool.share.service.impl;

import com.mpool.share.mapper.UserIpAddessMapper;
import com.mpool.share.service.UserIpaddessService;
import com.mpool.common.model.account.IpRegion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Service
public class UserIpaddessServiceImpl implements UserIpaddessService{

    @Autowired
    private UserIpAddessMapper userIpAddessMapper;
    @Override
    public void addIpAddess(String ip,Long userId) {
        IpRegion ipRegion = new IpRegion();
        //如果数据库已经有了该用户的登录Ip则返回，如果没有则获取保存数据库；
       IpRegion ipAdd = userIpAddessMapper.getIpAddess(ip);
       if (ipAdd != null){
           return;
       }
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> forEntity = restTemplate
                .getForEntity("http://freeapi.ipip.net/{ip}", String.class,ip);
        String fromJson =forEntity.getBody();
        String countries = fromJson.substring(2,4);
        String province = fromJson.substring(7,9);
        String city = fromJson.substring(12,14);
        String name = countries+"|"+province+"|"+city;
        ipRegion.setIp(ip);
        ipRegion.setRegion(name);
        ipRegion.setUserId(userId);
        ipRegion.setCreatedTime(new Date());
        userIpAddessMapper.insert(ipRegion);
    }

    @Override
    public void insert(IpRegion entity) {
    }

    @Override
    public void inserts(List<IpRegion> entitys) {

    }

    @Override
    public void update(IpRegion entity) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public IpRegion findById(Serializable id) {
        return null;
    }
}
