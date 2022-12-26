package com.gt.gateway;

import com.gt.gateway.loader.DynamicRouteLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.annotation.Resource;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

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
     *
     * @param args
     */
    @Override
    public void run(String... args) throws Exception {
        dynamicRouteLoader.refresh();
    }

    /**
     * 接口地址（通过9999端口直接访问）
     *
     * @param indexHtml
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> indexRouter(@Value("classpath:/META-INF/resources/doc.html") final org.springframework.core.io.Resource indexHtml) {
        return route(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).syncBody(indexHtml));
    }
}
