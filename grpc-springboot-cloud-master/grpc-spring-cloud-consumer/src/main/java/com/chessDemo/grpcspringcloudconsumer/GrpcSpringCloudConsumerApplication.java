package com.chessDemo.grpcspringcloudconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@SpringBootApplication
@EnableDiscoveryClient
public class GrpcSpringCloudConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcSpringCloudConsumerApplication.class, args);
    }
}
