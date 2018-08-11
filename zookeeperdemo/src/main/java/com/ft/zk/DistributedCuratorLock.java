package com.ft.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2018\8\10 0010.
 */
public class DistributedCuratorLock {

    static String lock_path = "/curator_locks";
    static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.3.140:2181").retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
    public static void main(String[] args) throws Exception {
        client.start();
        final InterProcessMutex lock = new InterProcessMutex(client, lock_path);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        for(int i = 0; i < 30; i ++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        countDownLatch.await();
                        lock.acquire();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                    String orderNo = sdf.format(new Date());
                    System.out.println("生成的订单号是：" + orderNo);
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

    }
}
