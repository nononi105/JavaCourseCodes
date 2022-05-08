package io.kimmking.rpcfx.demo.provider;

import java.util.HashMap;
import java.util.Set;


public class RpcServiceRegister {

    public HashMap<String, Class<?>> serviceContext = new HashMap<>();


    /***
     * 获取所有service目录下的实现类
     * @param servicePath
     */
    public RpcServiceRegister(String servicePath) {
        Set<Class<?>> implServiceList = ClassUtil.getClasses(servicePath);

        for (Class clz : implServiceList) {
            Class[] interfaces = clz.getInterfaces();
            for (Class inf: interfaces) {
                if (clz.getSimpleName().startsWith(inf.getSimpleName())) {
                    serviceContext.put(inf.getName(), clz);
                }
            }
        }
    }

    public HashMap<String, Class<?>> getServiceContext() {
        return serviceContext;
    }
}
