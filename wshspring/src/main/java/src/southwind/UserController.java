package src.southwind;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import src.entity.User;

/**
 * @author jmfen
 * date 2019-12-27
 */

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    public User getUserById(){
        return userService.getUserById(1);
    }
}