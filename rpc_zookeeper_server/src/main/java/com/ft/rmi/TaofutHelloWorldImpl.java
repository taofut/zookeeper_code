package com.ft.rmi;

import com.ft.rmi.anno.RpcAnnotation;

/**
 * Created by Administrator on 2018\6\9 0009.
 */
@RpcAnnotation(TaofutHelloWorld.class)
public class TaofutHelloWorldImpl implements TaofutHelloWorld {


    public String sayHello(String msg) {
        return "[我是8080的节点]Hello,World! My name is taofut "+msg;
    }
}
