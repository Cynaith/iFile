package com.cynaith.ifile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class IFileCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(IFileCommonApplication.class, args);
    }

}
