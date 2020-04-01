package Iocdemo;

import Iocdemo.bean.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * @author jmfen
 * date 2019-12-27
 */
public class app {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Address address = (Address)applicationContext.getBean("address");
        System.out.println(address);
    }
}