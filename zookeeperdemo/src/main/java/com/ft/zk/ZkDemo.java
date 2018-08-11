package com.ft.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * zookeeper节点特性
 */
public class ZkDemo {
    public static void main(String[] args) {
        try {
            final CountDownLatch countDownLatch=new CountDownLatch(1);
            ZooKeeper zookeeper=new
                    ZooKeeper("192.168.3.140:2181,192.168.3.141:2181,192.168.3.142:2181", 4000, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    if(Event.KeeperState.SyncConnected==watchedEvent.getState()){
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            System.out.println(zookeeper.getState());
                                //节点路径      值                 ACL（节点的操作权限）       节点的类型，持久化节点
            zookeeper.create("/zk-taofut","0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            Thread.sleep(1000);
            Stat stat=new Stat();

            //得到当前节点的值
            byte[] bytes=zookeeper.getData("/zk-taofut",null,stat);
            System.out.println(new String(bytes));

            //修改节点的值
            zookeeper.setData("/zk-taofut","1".getBytes(),stat.getVersion());

            //得到当前节点的值
            byte[] bytes1=zookeeper.getData("/zk-taofut",null,stat);
            System.out.println(new String(bytes1));

            //删除当前节点
            zookeeper.delete("/zk-taofut",stat.getVersion());

            zookeeper.close();

            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
