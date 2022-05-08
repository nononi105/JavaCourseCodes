package io.kimmking.rpcfx.demo.consumer.service;

import io.kimmking.rpcfx.client.aop.RpcService;
import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.OrderService;

public class OrderServiceImpl implements OrderService {

    @RpcService
    @Override
    public Order findOrderById(int id) {
        return null;
    }
}
