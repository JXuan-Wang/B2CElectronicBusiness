package org.example.system.common.log.aspect;

import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.system.common.log.annotation.Log;
import org.example.system.common.log.service.AsyncOperLogService;
import org.example.system.common.log.utils.LogUtil;
import org.example.system.model.entity.system.SysOperLog;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    @Resource
    private AsyncOperLogService asyncOperLogService;

    //环绕通知
    @Around(value="@annotation(sysLog)")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint, Log sysLog) throws Throwable {

//        String title=sysLog.title();
//        int businessType = sysLog.businessType();
//        System.out.println("title: "+title+" ::businessType: "+businessType);
        SysOperLog sysOperLog = new SysOperLog();
        LogUtil.beforeHandleLog(sysLog,joinPoint,sysOperLog);

        //业务方法
        Object proceed=null;
        try {
            proceed = joinPoint.proceed();

            LogUtil.afterHandleLog(sysLog,proceed,sysOperLog,0,null);

//            System.out.println("在业务方法之后执行....");
        }
        catch (Throwable e) {
            e.printStackTrace();
            LogUtil.afterHandleLog(sysLog,proceed,sysOperLog,1,e.getMessage());
            throw new RuntimeException();
        }
        //将日志信息添加数据库里面
        asyncOperLogService.saveSysOperLog(sysOperLog);
        return null;
    }
}
