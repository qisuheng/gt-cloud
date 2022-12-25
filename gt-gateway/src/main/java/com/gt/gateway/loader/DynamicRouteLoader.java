package com.gt.gateway.loader;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Lists;
import com.gt.gateway.config.GatewayRoutersConfig;
import com.gt.gateway.config.RouterDataType;
import com.gt.gateway.loader.repository.DynamicRouteService;
import com.gt.gateway.loader.repository.MyInMemoryRouteDefinitionRepository;
import com.gt.gateway.loader.vo.MyRouteDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @description: 动态路由加载器
 * @date: 2022/12/25 11:13
 * @author: qisuheng
 */
@Slf4j
@Component
@RefreshScope
@DependsOn({"gatewayRoutersConfig"})
public class DynamicRouteLoader implements ApplicationEventPublisherAware {
    public static final long DEFAULT_TIMEOUT = 30000;

    @Autowired
    private GatewayRoutersConfig gatewayRoutersConfig;
    private MyInMemoryRouteDefinitionRepository repository;
    private DynamicRouteService dynamicRouteService;
    private ApplicationEventPublisher publisher;
    private ConfigService configService;

    public DynamicRouteLoader(MyInMemoryRouteDefinitionRepository repository, DynamicRouteService dynamicRouteService) {
        this.repository = repository;
        this.dynamicRouteService = dynamicRouteService;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void init() {
        log.info("初始化路由模式，dataType：" + gatewayRoutersConfig.getDataType());
        if (RouterDataType.nacos.toString().endsWith(gatewayRoutersConfig.getDataType())) {
            loadRoutesByNacos();
        }
    }

    /**
     * 刷新路由
     *
     * @return
     */
    public Mono<Void> refresh() {
        log.info("初始化路由模式，dataType：" + gatewayRoutersConfig.getDataType());
        if (!RouterDataType.yml.toString().endsWith(gatewayRoutersConfig.getDataType())) {
            this.init();
        }
        return Mono.empty();
    }

    /**
     * 从nacos中读取路由配置
     *
     * @return
     */
    private void loadRoutesByNacos() {
        List<RouteDefinition> routes = Lists.newArrayList();
        configService = createConfigService();
        if (configService == null) {
            log.warn("initConfigService fail");
        }
        try {
            String configInfo = configService.getConfig(gatewayRoutersConfig.getDataId(), gatewayRoutersConfig.getRouteGroup(), DEFAULT_TIMEOUT);
            if (StringUtils.isNotBlank(configInfo)) {
                log.info("获取网关当前配置:\r\n{}", configInfo);
                routes = JSON.parseArray(configInfo, RouteDefinition.class);
            } else {
                log.warn("ERROR: 从Nacos获取网关配置为空，请确认Nacos配置是否正确！");
            }
        } catch (NacosException e) {
            log.error("初始化网关路由时发生错误", e);
            e.printStackTrace();
        }
        for (RouteDefinition definition : routes) {
            log.info("update route : {}", definition.toString());
            dynamicRouteService.add(definition);
        }
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        dynamicRouteByNacosListener(gatewayRoutersConfig.getDataId(), gatewayRoutersConfig.getRouteGroup());
    }

    /**
     * 监听Nacos下发的动态路由配置
     *
     * @param dataId
     * @param group
     */
    public void dynamicRouteByNacosListener(String dataId, String group) {
        try {
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("进行网关更新:\n\r{}", configInfo);
                    List<MyRouteDefinition> definitionList = JSON.parseArray(configInfo, MyRouteDefinition.class);
                    for (MyRouteDefinition definition : definitionList) {
                        log.info("update route : {}", definition.toString());
                        dynamicRouteService.update(definition);
                    }
                }

                @Override
                public Executor getExecutor() {
                    log.info("getExecutor\n\r");
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("从nacos接收动态路由配置出错!!!", e);
        }
    }

    /**
     * 创建ConfigService
     *
     * @return
     */
    private ConfigService createConfigService() {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", gatewayRoutersConfig.getServerAddr());
            if (StringUtils.isNotBlank(gatewayRoutersConfig.getNamespace())) {
                properties.setProperty("namespace", gatewayRoutersConfig.getNamespace());
            }
            if (StringUtils.isNotBlank(gatewayRoutersConfig.getUsername())) {
                properties.setProperty("username", gatewayRoutersConfig.getUsername());
            }
            if (StringUtils.isNotBlank(gatewayRoutersConfig.getPassword())) {
                properties.setProperty("password", gatewayRoutersConfig.getPassword());
            }
            return configService = NacosFactory.createConfigService(properties);
        } catch (Exception e) {
            log.error("创建ConfigService异常", e);
            return null;
        }
    }
}
