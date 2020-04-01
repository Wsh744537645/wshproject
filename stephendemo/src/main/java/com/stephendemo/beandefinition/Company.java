package com.stephendemo.beandefinition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jmfen
 * date 2020-03-16
 */

@Configuration
public class Company {

    @Bean
    public Staff littleMing(){
        return new Staff();
    }

    @Bean
    public Staff littleHua(){
        return new Staff();
    }
}