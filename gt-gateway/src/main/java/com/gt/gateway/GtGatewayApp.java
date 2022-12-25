package com.gt.gateway;

import com.gt.gateway.loader.DynamicRouteLoader;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description: GtGatewayApp
 * @date: 2022/12/25 10:47
 * @author: qisuheng
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class GtGatewayApp implements CommandLineRunner {

    @Resource
    private DynamicRouteLoader dynamicRouteLoader;

    public static void main(String[] args) {
        SpringApplication.run(GtGatewayApp.class, args);
    }

    /**
     * 容器初始化后加载路由
     * @param args
     */
    @Override
    public void run(String... args) throws Exception {
        dynamicRouteLoader.refresh();
    }
}
