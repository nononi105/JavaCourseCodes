package io.nononi.week5.annotation;

import org.springframework.stereotype.Component;

/**
 * 自动注入样例
 */
@Component
public class AnnotationExample {
    public void printMsg(){
        System.out.println("通过注解自动注入");
    }
}
