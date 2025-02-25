package io.nononi.dynamicdb;

import io.nononi.dynamicdb.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class DynamicdbApplicationTests {
    @Autowired
    private OrderServiceImpl orderService;

    @Test
    public void testDynamicDb(){
        orderService.insert(4,"ohhhhhh");
        orderService.query();
    }

}
