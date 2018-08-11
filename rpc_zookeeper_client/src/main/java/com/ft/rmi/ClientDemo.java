package com.ft.rmi;

import com.ft.rmi.zk.IServiceDiscovery;
import com.ft.rmi.zk.ServiceDiscoveryImpl;
import com.ft.rmi.zk.ZkConfig;

/**
 * Created by Administrator on 2018\6\9 0009.
 */
public class ClientDemo {

    public static void main(String[] args) throws InterruptedException {
        IServiceDiscovery serviceDiscovery=new ServiceDiscoveryImpl(ZkConfig.CONNECTION_STR);
        RpcClientProxy rpcClientProxy=new RpcClientProxy(serviceDiscovery);

        for(int i=0;i<10;i++){
            //客户端使用代理类来发起请求
            TaofutHelloWorld taofutHelloWorld=(TaofutHelloWorld) rpcClientProxy.clientProxy(TaofutHelloWorld.class,null);
            System.out.println(taofutHelloWorld.sayHello("我是架构师"));
            Thread.sleep(1000);
        }

    }
}
