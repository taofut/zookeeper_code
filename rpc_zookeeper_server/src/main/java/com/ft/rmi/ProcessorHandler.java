package com.ft.rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * 处理客户端发来的请求
 */
public class ProcessorHandler implements Runnable {

    private Socket socket;
    Map<String,Object> handlerMap;

    public ProcessorHandler(Socket socket, Map<String,Object> handlerMap) {
        this.socket = socket;
        this.handlerMap = handlerMap;
    }

    public void run() {
        //处理socket请求，服务端接收数据，从输入流里读
        ObjectInputStream objectInputStream=null;
        ObjectOutputStream outputStream=null;
        try {
            objectInputStream=new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest=(RpcRequest) objectInputStream.readObject();
            Object result=invoke(rpcRequest);

            //将服务端执行的结果 通过socket回传给客户端
            outputStream=new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(result);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(objectInputStream!=null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 客户端：发来请求告诉服务端，我要调用你这个服务里面的xx方法，并将xx方法的详细信息传递给服务端。
     * 服务端：收到请求及参数后，利用反射去执行了xx方法，并将执行的结果返回给客户端。
     * @param rpcRequest
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    private Object invoke(RpcRequest rpcRequest) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Object[] args=rpcRequest.getParameters();
        Class<?>[] types=new Class[args.length];
        for(int i=0;i<args.length;i++){
            types[i]=args[i].getClass();
        }
        String serviceName=rpcRequest.getClassName();
        String version=rpcRequest.getVersion();
        if(version!=null&&!version.equals("")){
            serviceName=serviceName+"-"+version;
        }
        //中handlerMap中，根据客户端请求的地址，去拿到响应的服务，通过反射发起调用
        Object service=handlerMap.get(serviceName);
        Method method=service.getClass().getMethod(rpcRequest.getMethodName(),types);
        return method.invoke(service,args);
    }
}
