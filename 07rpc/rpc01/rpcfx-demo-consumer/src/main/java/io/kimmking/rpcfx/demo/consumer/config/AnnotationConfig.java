package io.kimmking.rpcfx.demo.consumer.config;


import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"io.kimmking.rpcfx.demo.consumer.service","io.kimmking.rpcfx.client.aop"})
public class AnnotationConfig {
    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }
}