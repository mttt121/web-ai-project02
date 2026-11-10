package org.example.aop;

import org.example.anno.Log;
import org.example.mapper.OperateLogMapper;
import org.example.pojo.OperateLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.utils.CurrentHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class OperationLogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    // 环绕通知（参数改名 logAnno，避免和 Lombok 的 log 字段重名）
    @Around("@annotation(logAnno)")
    public Object around(ProceedingJoinPoint joinPoint, Log logAnno) throws Throwable {
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        // 执行业务方法（业务自身的异常照常向外抛）
        Object result = joinPoint.proceed();
        long costTime = System.currentTimeMillis() - startTime;

        // 写日志是附加动作，任何失败都不能连累已经成功的业务方法
        try {
            OperateLog operateLog = new OperateLog();
            operateLog.setOperateEmpId(getCurrentUserId());
            operateLog.setOperateTime(LocalDateTime.now());
            operateLog.setClassName(truncate(joinPoint.getTarget().getClass().getName(), 100));
            operateLog.setMethodName(truncate(joinPoint.getSignature().getName(), 100));
            operateLog.setMethodParams(truncate(Arrays.toString(joinPoint.getArgs()), 1000));
            operateLog.setReturnValue(truncate(result == null ? "void" : result.toString(), 2000));
            operateLog.setCostTime(costTime);
            operateLogMapper.insert(operateLog);
        } catch (Exception e) {
            log.error("记录操作日志失败: {}", e.getMessage(), e);
        }
        return result;
    }

    /** 从 ThreadLocal 取当前登录人ID，未登录时返回 null */
    private Integer getCurrentUserId() {
        return CurrentHolder.getCurrentId();
    }

    /** 按表字段长度截断，避免参数过长导致 Data too long */
    private String truncate(String value, int maxLength) {
        if (value == null) return "";
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}