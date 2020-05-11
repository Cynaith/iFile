package com.cynaith.ifile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class IFileServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IFileServerApplication.class, args);
    }

}
