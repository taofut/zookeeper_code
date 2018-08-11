package com.ft.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * 监听事件：Watcher只会监听一次事件，如果需要一直监听，那么要循环了
 */
public class WatcherDemo {
    public static void main(String[] args) {
        try {
            final CountDownLatch countDownLatch=new CountDownLatch(1);
            final ZooKeeper zookeeper=new
                    ZooKeeper("192.168.3.140:2181,192.168.3.141:2181,192.168.3.142:2181", 4000, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("默认事件："+watchedEvent.getType());
                    if(Event.KeeperState.SyncConnected==watchedEvent.getState()){
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();

                                //节点路径      值                 ACL（节点的操作权限）       节点的类型，持久化节点
            zookeeper.create("/zk-taofut","0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            //绑定事件条件：exists getdata getchildren
            //通过exists绑定事件
            Stat stat=zookeeper.exists("/zk-taofut", new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    System.out.println(watchedEvent.getType()+"->"+watchedEvent.getPath());
                    //Watcher只能监听一次，所以在这里要做循环
                    try {
                        zookeeper.exists("/zk-taofut",true);//这里的true，表示指代默认的Watcher(以上第17行)
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            //通过事务-修改来触发监听事件
            stat=zookeeper.setData("/zk-taofut","2".getBytes(),stat.getVersion());

            Thread.sleep(1000);

            zookeeper.delete("/zk-taofut",stat.getVersion());

            System.in.read();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
