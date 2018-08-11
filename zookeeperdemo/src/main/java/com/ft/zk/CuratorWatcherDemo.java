package com.ft.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 事件监听
 * PathChildCache 监听一个节点下，子节点的创建、删除、更新
 * NodeCache 监听一个节点的创建和更新
 * TreeCathe 综合PathChildCache和NodeCache的特性
 */
public class CuratorWatcherDemo {

    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework= CuratorFrameworkFactory.
                builder().connectString("192.168.3.140:2181,192.168.3.141:2181,192.168.3.142:2181").
                sessionTimeoutMs(4000).retryPolicy(new
                ExponentialBackoffRetry(1000,3)).
                namespace("curator").build();
        curatorFramework.start();
        //监听/taofut节点下的子节点
//        addListenerWithPathChildCache(curatorFramework,"/taofut");
        //监听/taofut节点
//        addListenerWithNodeCache(curatorFramework,"/taofut");
        //监听/taofut节点及子节点
        addListenerWithTreeCache(curatorFramework,"/taofut");
        System.in.read();//进程停留
    }

    /**
     * PathChildCache 监听一个节点下，子节点的创建、删除、更新
     * @param curatorFramework
     * @param path
     * @throws Exception
     */
    public static void addListenerWithPathChildCache(CuratorFramework curatorFramework,String path) throws Exception {
        final PathChildrenCache pathChildrenCache=new PathChildrenCache(curatorFramework,path,true);
        PathChildrenCacheListener pathChildrenCacheListener=new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                System.out.println("收到事件："+pathChildrenCacheEvent.getData().getPath());
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start(PathChildrenCache.StartMode.NORMAL);
    }

    /**
     * NodeCache 监听一个节点的创建和更新
     * @param curatorFramework
     * @param path
     * @throws Exception
     */
    public static void addListenerWithNodeCache(CuratorFramework curatorFramework,String path) throws Exception {
        final NodeCache nodeCache=new NodeCache(curatorFramework,path,false);
        NodeCacheListener nodeCacheListener=new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println("收到事件："+nodeCache.getCurrentData().getPath());
            }
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
    }

    /**
     * TreeCathe 综合PathChildCache和NodeCache的特性
     * @param curatorFramework
     * @param path
     * @throws Exception
     */
    public static void addListenerWithTreeCache(CuratorFramework curatorFramework,String path) throws Exception {
        final TreeCache treeCache=new TreeCache(curatorFramework,path);
        TreeCacheListener treeCacheListener=new TreeCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                System.out.println("收到事件："+treeCacheEvent.getType()+"->"+treeCacheEvent.getData().getPath());
            }
        };
        treeCache.getListenable().addListener(treeCacheListener);
        treeCache.start();
    }

}
