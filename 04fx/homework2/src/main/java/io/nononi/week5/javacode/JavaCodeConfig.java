package io.nononi.week5.javacode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * java配置类
 */
@Configuration
public class JavaCodeConfig {

    @Bean
    public JavaCodeExample javaCodeExample(){
        return new JavaCodeExample();
    }
}
