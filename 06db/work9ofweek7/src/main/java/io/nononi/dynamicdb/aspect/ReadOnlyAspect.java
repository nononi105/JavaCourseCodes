package io.nononi.dynamicdb.aspect;

import io.nononi.dynamicdb.annotation.ReadOnly;
import io.nononi.dynamicdb.datasource.DynamicDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class ReadOnlyAspect {

    @Pointcut("@annotation(io.nononi.dynamicdb.annotation.ReadOnly)")
    public void readOnly(){};


    @Around("readOnly()")
    public Object around(ProceedingJoinPoint point) throws Throwable{
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        ReadOnly readOnly = method.getAnnotation(ReadOnly.class);
        if(readOnly == null){
            DynamicDataSource.setDataSource("master");
            System.out.println("datasource is :" + "master");
        } else {
            DynamicDataSource.setDataSource("slave");
            System.out.println("datasource is :" + "slave");
        }

        try {
            return point.proceed();
        }finally {
            DynamicDataSource.clearDataSource();
            System.out.println("clean datasource");
        }
    }

}
