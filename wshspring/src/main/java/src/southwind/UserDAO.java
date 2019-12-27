package src.southwind;

import org.springframework.stereotype.Repository;
import src.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jmfen
 * date 2019-12-27
 */

@Repository
public class UserDAO {
    private static Map<Integer,User> users;

    static{
        users = new HashMap<Integer,User>();
        users.put(1, new User(1, "张三"));
        users.put(2, new User(2, "李四"));
        users.put(3, new User(3, "王五"));
    }

    public User getUserById(int id) {
        // TODO Auto-generated method stub
        return users.get(id);
    }
}