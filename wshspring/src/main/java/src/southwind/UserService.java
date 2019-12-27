package src.southwind;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.entity.User;

/**
 * @author jmfen
 * date 2019-12-27
 */

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    public User getUserById(int id) {
        // TODO Auto-generated method stub
        return userDAO.getUserById(id);
    }
}