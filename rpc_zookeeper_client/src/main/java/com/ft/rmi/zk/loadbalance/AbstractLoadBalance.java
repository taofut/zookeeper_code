package com.ft.rmi.zk.loadbalance;

import java.util.List;

/**
 * Created by Administrator on 2018\8\11 0011.
 */
public abstract class AbstractLoadBalance implements LoadBalance{

    public String selectHost(List<String> repos) {
        if(repos==null&&repos.size()==0){
            return null;
        }
        if(repos.size()==1){
            return repos.get(0);
        }
        return doselect(repos);
    }

    protected abstract String doselect(List<String> repos);
}
