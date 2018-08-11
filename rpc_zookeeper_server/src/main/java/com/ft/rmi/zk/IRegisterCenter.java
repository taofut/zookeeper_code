package com.ft.rmi.zk;

/**
 * Created by Administrator on 2018\8\10 0010.
 */
public interface IRegisterCenter {

    /**
     * 注册服务名称和服务地址
     * @param serviceName
     * @param serviceAddress
     */
    void register(String serviceName, String serviceAddress);
}
