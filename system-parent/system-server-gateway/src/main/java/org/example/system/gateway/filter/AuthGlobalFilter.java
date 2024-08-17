package org.example.system.gateway.filter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.example.system.model.entity.user.UserInfo;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;


public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Resource
    private AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求路径
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 判断路径 /api/**/auth/**，登录校验
        if(matcher.match("/api/**/auth/**",path)){
            // 登录校验
            UserInfo userInfo=getUserInfo(request);
            if(userInfo==null){
                ServerHttpResponse response = exchange.getResponse();
                return out(response,ResultCodeEnum.LOGIN_AUTH);
            }
        }

        //放行
        return chain.filter(exchange);
    }

    private UserInfo getUserInfo(ServerHttpRequest request) {
        String token="";
        //从请求头获取token
        List<String> tokenList = request.getHeaders().get("token");
        if(tokenList !=null){
            token=tokenList.get(0);
        }
        //判断token是否为空
        if(!StringUtil.isEmpty(token)){
            //根据token查询redis
            String userJson = redisTemplate.opsForValue().get("user:system" + token);
            if(StringUtils.isEmpty(userJson)){
                return null;
            }
            return JSON.parseObject(userJson, UserInfo.class);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        Result result = Result.build(null, resultCodeEnum);
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}
