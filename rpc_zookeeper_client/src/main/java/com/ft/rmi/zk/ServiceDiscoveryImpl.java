package com.ft.rmi.zk;

import com.ft.rmi.zk.loadbalance.LoadBalance;
import com.ft.rmi.zk.loadbalance.RandomLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\8\11 0011.
 */
public class ServiceDiscoveryImpl implements IServiceDiscovery {

    private List<String> repos=new ArrayList();
    private CuratorFramework curatorFramework;
    private String address;

    public ServiceDiscoveryImpl(String address) {
        this.address = address;
        curatorFramework= CuratorFrameworkFactory.builder().
                connectString(address).
                sessionTimeoutMs(4000).
                retryPolicy(new ExponentialBackoffRetry(1000,10)).build();
        curatorFramework.start();
    }


    public String discover(String serviceName) {
        String path=ZkConfig.ZK_REGISTER_PATH+"/"+serviceName;
        try{
            repos=curatorFramework.getChildren().forPath(path);
        }catch (Exception e){
            throw new RuntimeException("获取子节点异常："+e);
        }
        //动态发现服务节点的变化（监听）
        registerWatcher(path);

        //负载均衡机制-随机选择一个服务进行调用
        LoadBalance loadBalance=new RandomLoadBalance();
        return loadBalance.selectHost(repos);
    }

    private void registerWatcher(final String path){
        PathChildrenCache childrenCache=new PathChildrenCache(curatorFramework,path,true);

        PathChildrenCacheListener pathChildrenCacheListener=new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                repos=curatorFramework.getChildren().forPath(path);
            }
        };

        childrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            childrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException("注册PathChild Watcher异常："+e);
        }
    }
}










