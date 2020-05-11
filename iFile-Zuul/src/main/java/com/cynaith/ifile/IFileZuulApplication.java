package com.cynaith.ifile;

import com.cynaith.ifile.filter.PreAuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class IFileZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(IFileZuulApplication.class, args);
    }
}
