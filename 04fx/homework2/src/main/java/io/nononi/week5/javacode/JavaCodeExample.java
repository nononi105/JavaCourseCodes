package io.nononi.week5.javacode;

import org.springframework.stereotype.Component;

/**
 * 待装配的Bean
 */
@Component
public class JavaCodeExample {
    public void printMsg(){
        System.out.println("Java配置类注入");
    }
}
