package com.gt.gateway.loader.vo;

import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * @description: 自定义RouteDefinition
 * @date: 2022/12/25 12:25
 * @author: qisuheng
 */
public class MyRouteDefinition extends RouteDefinition {
    /**
     * 路由状态
     */
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
