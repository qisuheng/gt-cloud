package com.gt.gateway.fallback;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.gt.common.enums.SentinelErrorInfoEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * 功能描述：自定义Sentinel全局异常(需要启动Sentinel客户端)
 *
 * @Author: qisuheng
 * @Date: 2022/12/26 14:52
 */
@Configuration
public class GatewaySentinelExceptionConfig {

    @PostConstruct
    public void init() {

        BlockRequestHandler blockRequestHandler = (serverWebExchange, ex) -> {
            String msg;
            SentinelErrorInfoEnum errorInfoEnum = SentinelErrorInfoEnum.getErrorByException(ex);
            if (ObjectUtil.isNotEmpty(errorInfoEnum)) {
                msg = errorInfoEnum.getError();
            } else {
                msg = "未知限流降级";
            }
            HashMap<String, String> map = new HashMap(5);
            map.put("code", HttpStatus.TOO_MANY_REQUESTS.toString());
            map.put("message", msg);
            //自定义异常处理
            return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(map));
        };

        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }
}
