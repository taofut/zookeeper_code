package com.ft.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Created by Administrator on 2018\8\4 0004.
 */
public class CuratorDemo {

    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework= CuratorFrameworkFactory.
                builder().connectString("192.168.3.140:2181,192.168.3.141:2181,192.168.3.142:2181").
                sessionTimeoutMs(4000).retryPolicy(new
                ExponentialBackoffRetry(1000,3)).
                namespace("curator").build();
        curatorFramework.start();

        //创建
        //结果：/curator/taofut/node1
        //原生的zk API中必须逐层创建，也就是说必须存在父节点才能创建子节点
        curatorFramework.create().creatingParentContainersIfNeeded().
                withMode(CreateMode.PERSISTENT).forPath("/taofut/node1","1".getBytes());

        //修改
        Stat stat=new Stat();
        curatorFramework.getData().storingStatIn(stat).forPath("/taofut/node1");

        curatorFramework.setData().withVersion(stat.getVersion()).
                forPath("/taofut/node1","xx".getBytes());

        //删除
        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/taofut/node1");

        curatorFramework.close();

    }
}
