package com.ft.rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2018\6\9 0009.
 */
public class TCPTransport {
    private String serviceAddress;

    public TCPTransport(String serviceAddress) {
        this.serviceAddress=serviceAddress;
    }

    public Socket newSocket(){
        System.out.println("创建一个新的连接");
        Socket socket=null;
        try {
            //与服务端建立连接
            String[] addrs=serviceAddress.split(":");
            socket=new Socket(addrs[0],Integer.parseInt(addrs[1]));
            return socket;
        } catch (IOException e) {
            throw new RuntimeException("连接建立失败");
        }

    }

    /**
     * 与服务端建立连接，并且向服务端传输 rpcRequest(服务端需要执行的方法详细信息)
     * 收到服务端执行完方法的结果
     * @param rpcRequest
     * @return
     */
    public Object send(RpcRequest rpcRequest){
        Socket socket=null;
        try {
            socket=newSocket();
            ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(rpcRequest);//rpcRequest序列化传输
            outputStream.flush();

            ObjectInputStream inputStream=new ObjectInputStream(socket.getInputStream());
            Object result=inputStream.readObject();
            inputStream.close();
            outputStream.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("发起远程调用异常",e);
        } finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
