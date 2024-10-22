package org.example.system.manager.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.system.manager.finalArgs.RedisKeyPrefix;
import org.example.system.model.entity.system.SysUser;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.example.system.utils.AuthContextUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class LoginAuthInterceptor implements HandlerInterceptor {
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        //获取请求方式
        //如果请求方式是options 预检请求，直接放行
        String method = request.getMethod();
        if("OPTIONS".equals(method)){
            return true;
        }

        //从请求头获取token
        String token = request.getHeader("token");

        //如果token为空，返回错误提示
        if(StrUtil.isEmpty(token)){
            responseNoLoginInfo(response);
            return false;
        }

        //如果token不为空，拿着token查询redis
        String userInfoString = redisTemplate.opsForValue().get(RedisKeyPrefix.USER_LOGIN + token);

        //如果redis查询不到数据，返回错误提示
        if(StrUtil.isEmpty(userInfoString)){
            responseNoLoginInfo(response);
            return false;
        }

        //如果redis查询到用户信息，把用户信息放到ThreadLocal里面
        AuthContextUtil.set(JSON.parseObject(userInfoString, SysUser.class));

        //把redis用户信息数据更新过期时间
        redisTemplate.expire(RedisKeyPrefix.USER_LOGIN+token,30, TimeUnit.MINUTES);

        //放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //ThreadLocal删除
        AuthContextUtil.remove();
    }

    //响应208状态码给前端
    private void responseNoLoginInfo(HttpServletResponse response) {
        Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }
}
