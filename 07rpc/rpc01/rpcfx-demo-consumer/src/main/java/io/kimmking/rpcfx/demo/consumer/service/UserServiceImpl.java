package io.kimmking.rpcfx.demo.consumer.service;

import io.kimmking.rpcfx.client.aop.RpcService;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @RpcService
    @Override
    public User findById(int id) {
        return null;
    }
}
