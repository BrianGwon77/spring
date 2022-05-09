package com.example.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LogAop {

    @Pointcut("execution(* com.example.spring.controller..*.*(..))")
    public void log() {}

    @Before("log()")
    public void before(JoinPoint joinPoint){

        List<String> authorities = ( SecurityContextHolder.getContext().getAuthentication() == null ) ? null :
                SecurityContextHolder.getContext().getAuthentication()
                        .getAuthorities().stream().map(authority -> authority.toString()).collect(Collectors.toList());

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String name = method.getName();

        ZoneId zoneId = TimeZone.getTimeZone("Asia/Seoul").toZoneId();
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);

        log.info("Time : {}", localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
        log.info("Method Name : {}",  name);
        log.info("----- Parameter List -----");

        log.info("{}", Arrays.stream(joinPoint.getArgs()).map(arg->arg.toString()).collect(Collectors.toList()));

        log.info("Authorities : {}", authorities);

    }

}
