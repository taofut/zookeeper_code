package com.ft.zk;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2018\8\7 0007.
 */
public class DistributedLockTest {

    public static void main(String[] args) throws IOException {
        final CountDownLatch countDownLatch=new CountDownLatch(10);
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {
                public void run() {
                    try {
                        countDownLatch.await();
                        //10个线程一起并发执行以下代码
                        DistributedLock distributedLock=new DistributedLock();
                        distributedLock.lock();//当前线程获得锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },"Thread-"+i).start();
            countDownLatch.countDown();
        }
        System.in.read();
    }
}
