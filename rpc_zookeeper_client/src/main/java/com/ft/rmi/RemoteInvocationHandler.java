package com.ft.rmi;

import com.ft.rmi.zk.IServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018\6\9 0009.
 */
public class RemoteInvocationHandler implements InvocationHandler {

    private IServiceDiscovery serviceDiscovery;
    private String version;

    public RemoteInvocationHandler(IServiceDiscovery serviceDiscovery,String version) {
        this.serviceDiscovery = serviceDiscovery;
        this.version=version;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest=new RpcRequest();
        //例：className=com.ft.rmi.TaofutHelloWorld
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        //例：methodName=sayHello
        rpcRequest.setMethodName(method.getName());
        //例：parameters=我是架构师
        rpcRequest.setParameters(args);
        rpcRequest.setVersion(version);
        //通过接口名称拿到对应的服务地址
        String serviceAddress=serviceDiscovery.discover(rpcRequest.getClassName());
        //传输
        TCPTransport tcpTransport=new TCPTransport(serviceAddress);
        //向服务端发送请求，并且传输 rpcRequest
        return tcpTransport.send(rpcRequest);
    }


}
