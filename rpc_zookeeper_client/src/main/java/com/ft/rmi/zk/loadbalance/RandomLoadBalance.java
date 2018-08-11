package com.ft.rmi.zk.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * 负载均衡-随机算法：多个服务地址情况下，用来随机选择服务地址
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doselect(List<String> repos) {
        int len=repos.size();
        Random random=new Random();
        return repos.get(random.nextInt(len));
    }
}
