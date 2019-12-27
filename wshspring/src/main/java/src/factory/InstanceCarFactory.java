package src.factory;

import src.entity.Car;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jmfen
 * date 2019-12-27
 */
public class InstanceCarFactory {
    private Map<Integer,Car> cars;
    public InstanceCarFactory() {
        cars = new HashMap<Integer,Car>();
        cars.put(1, new Car(1,"奥迪"));
        cars.put(2, new Car(2,"奥拓"));
    }
    public Car getCar(int num){
        return cars.get(num);
    }
}