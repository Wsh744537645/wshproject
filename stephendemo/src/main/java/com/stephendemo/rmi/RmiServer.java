package com.stephendemo.rmi;

import com.stephendemo.rmi.service.RemoteHello;
import com.stephendemo.rmi.service.RemoteHelloImpl;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author jmfen
 * date 2020-04-20
 */
public class RmiServer {

    public static void main(String[] args) {
        RemoteHello remoteHello = new RemoteHelloImpl();
        try {
            RemoteHello stub = (RemoteHello)UnicastRemoteObject.exportObject(remoteHello, 8001);
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 8000);
            //使用名字hello，将服务注册到Registry
            registry.bind("hello", stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

    }
}