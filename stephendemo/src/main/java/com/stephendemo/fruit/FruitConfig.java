package com.stephendemo.fruit;

import org.springframework.context.annotation.Configuration;

/**
 * @author jmfen
 * date 2020-03-16
 */

@EnableFruit(fruit = "apple", weight = 24.0d, color = "red")
@Configuration
public class FruitConfig {
}