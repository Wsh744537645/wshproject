package com.stephendemo.mongo;

import com.mongodb.client.result.UpdateResult;
import com.stephendemo.data.User;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jmfen
 * date 2020-04-13
 */

@Service
public class MongoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> add(User user){
        mongoTemplate.insert(user, "user");

        return mongoTemplate.findAll(User.class);
    }

    public List<User> findOne(String name){
        Query query = new Query();
        return mongoTemplate.find(query.addCriteria(Criteria.where("name").is(name)), User.class);
    }

    public UpdateResult updateOne(User user){
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(user.getName()));
        Update update = new Update();
        update.set("aget", user.getAget());
        update.set("add", user.getAdd());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, "user");
        return updateResult;
    }

    public Object removeOne(String name){
        return mongoTemplate.remove(new Query().addCriteria(Criteria.where("name").is(name)), User.class);
    }
}