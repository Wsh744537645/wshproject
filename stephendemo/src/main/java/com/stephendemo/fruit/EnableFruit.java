package com.stephendemo.fruit;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author jmfen
 * date 2020-03-16
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RegFruitBd.class)
public @interface EnableFruit {
    String fruit();

    double weight();

    String color();
}
