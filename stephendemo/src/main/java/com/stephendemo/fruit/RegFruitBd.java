package com.stephendemo.fruit;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author jmfen
 * date 2020-03-16
 */
public class RegFruitBd implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> att = importingClassMetadata.getAnnotationAttributes(EnableFruit.class.getName());
        String fruit = att.get("fruit").toString();
        if("apple".equals(fruit)){
            return new String[]{Apple.class.getName()};
        }else if("orange".equals("orange")){
            return new String[]{Orange.class.getName()};
        }
        return new String[0];
    }
}