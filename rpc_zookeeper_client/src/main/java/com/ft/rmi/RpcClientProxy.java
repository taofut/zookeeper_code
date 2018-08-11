package com.ft.rmi;

import com.ft.rmi.zk.IServiceDiscovery;

import java.lang.reflect.Proxy;

/**
 * 创建一个客户端代理类
 */
public class RpcClientProxy {

    private IServiceDiscovery serviceDiscovery;

    public RpcClientProxy(IServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T clientProxy(final Class<T> interfaceCls,String version){
        return (T)Proxy.newProxyInstance(interfaceCls.getClassLoader(),
                new Class[]{interfaceCls},new RemoteInvocationHandler(serviceDiscovery,version));
    }
}
