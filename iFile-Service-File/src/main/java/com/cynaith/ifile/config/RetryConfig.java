package com.cynaith.ifile.config;

import com.github.houbb.sisyphus.spring.annotation.EnableRetry;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: Cynaith
 **/

@Configurable
@ComponentScan(basePackages = "com.cynaith.ifile.service.impl")
@EnableRetry
public class RetryConfig {
}
