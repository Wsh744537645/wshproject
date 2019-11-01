package com.stephendemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient
public class StephendemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(StephendemoApplication.class, args);
    }

}
