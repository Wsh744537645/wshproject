package com.stephendemo.rmi;

import com.stephendemo.rmi.service.RemoteHello;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author jmfen
 * date 2020-04-20
 */
public class RmiClient {
    public static void main(String[] args) {
        try {
            //获取注册中心引用
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 8000);
            //获取RemoteHello服务
            RemoteHello remoteHello = (RemoteHello)registry.lookup("hello");
            //调用远程方法
            System.out.println(String.format("result: %s", remoteHello.sayHello("test")));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}