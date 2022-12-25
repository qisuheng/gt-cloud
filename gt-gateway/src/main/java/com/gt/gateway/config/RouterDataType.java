package com.gt.gateway.config;

/**
 * @description: nocos配置方式枚举
 * @date: 2022/12/25 12:17
 * @author: qisuheng
 */
public enum RouterDataType {
    /**
     * 数据库加载路由配置
     */
    database,
    /**
     * 本地yml加载路由配置
     */
    yml,
    /**
     * nacos加载路由配置
     */
    nacos
}
