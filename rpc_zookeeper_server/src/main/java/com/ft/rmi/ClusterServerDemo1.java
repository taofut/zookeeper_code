package com.ft.rmi;

import com.ft.rmi.zk.IRegisterCenter;
import com.ft.rmi.zk.RegisterCenterImpl;

import java.io.IOException;

/**
 * Created by Administrator on 2018\6\9 0009.
 */
public class ClusterServerDemo1 {

    public static void main(String[] args) throws IOException {
        TaofutHelloWorld taofutHelloWorld=new TaofutHelloWorldImpl1();
        IRegisterCenter registerCenter=new RegisterCenterImpl();

        RpcServer rpcServer=new RpcServer(registerCenter,"127.0.0.1:8081");
        rpcServer.bind(taofutHelloWorld);
        rpcServer.publisher();
        System.in.read();

    }
}
