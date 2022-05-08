package io.kimmking.rpcfx.demo.consumer;


import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import io.kimmking.rpcfx.demo.consumer.config.AnnotationConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AnnotationConfig.class)
public class AopTest {


    @Autowired
    private UserService userService;

    @Test
    public void testAop() {
        User user = userService.findById(1);
        System.out.println("find user id=1 from server: " + user.getName());

    }
}
