package src;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import src.bean.Student;
import src.entity.Car;
import src.southwind.UserController;

/**
 * @author jmfen
 * date 2019-12-26
 */
public class application {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        Student student = context.getBean(Student.class);
        System.out.println(student.getAge() + "---" + student.getName());

        Car car1 = (Car) context.getBean("car1");
        System.out.println(car1);

        Car car2 = (Car) context.getBean("car2");
        System.out.println(car2);

        UserController userController = context.getBean(UserController.class);
        System.out.println(userController.getUserById());
    }
}