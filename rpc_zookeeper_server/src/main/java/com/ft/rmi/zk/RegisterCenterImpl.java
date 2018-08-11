package com.ft.rmi.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * Created by Administrator on 2018\8\10 0010.
 */
public class RegisterCenterImpl implements IRegisterCenter {

    private CuratorFramework curatorFramework;


    //连接zookeeper
    {
        curatorFramework= CuratorFrameworkFactory.builder().
                connectString(com.ft.rmi.zk.ZkConfig.CONNECTION_STR).
                sessionTimeoutMs(4000).
                retryPolicy(new ExponentialBackoffRetry(1000,10)).build();
        curatorFramework.start();
    }


    public void register(String serviceName, String serviceAddress) {
        //注册相应的服务
        try {
            //例：servicePath=/registrys/com.ft.rmi.TaofutHelloWorld
            String servicePath= ZkConfig.ZK_REGISTER_PATH + "/" +serviceName;
            //如果节点不存在，则创建
            if(curatorFramework.checkExists().forPath(servicePath)==null){
                curatorFramework.create().creatingParentsIfNeeded().
                        withMode(CreateMode.PERSISTENT).forPath(servicePath,"0".getBytes());
            }
            //例：addressPath=/registrys/com.ft.rmi.TaofutHelloWorld/127.0.0.1:8080
            String addressPath=servicePath+"/"+serviceAddress;
            //创建临时节点：/registrys/com.ft.rmi.TaofutHelloWorld/127.0.0.1:8080
            String rsNode=curatorFramework.create().withMode(CreateMode.EPHEMERAL).
                    forPath(addressPath,"0".getBytes());
            System.out.println("服务注册成功："+rsNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
