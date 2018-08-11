package com.ft.rmi.zk.loadbalance;

import java.util.List;

/**
 * Created by Administrator on 2018\8\11 0011.
 */
public interface LoadBalance {

    String selectHost(List<String> repos);
}
