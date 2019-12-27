package src.factory;

import src.entity.Car;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jmfen
 * date 2019-12-27
 */
public class StaticCarFactory {
    private static Map<Integer, Car> cars;
    static {
        cars = new HashMap<Integer, Car>();
        cars.put(1, new Car(1, "奥迪"));
        cars.put(2, new Car(2, "奥拓"));
    }

    public static Car getCar(Integer num) {
        return cars.get(num);
    }
}