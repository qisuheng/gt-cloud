package com.gt.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @description: 监控服务
 * @date: 2022/12/21 19:03
 * @author: qisuheng
 */
@EnableDiscoveryClient
@EnableAdminServer
@SpringBootApplication
public class GtMonitorApp {
    public static void main(String[] args) {
        SpringApplication.run(GtMonitorApp.class, args);
    }
}
