package com.stephendemo.rmi.service;

import java.rmi.RemoteException;

/**
 * @author jmfen
 * date 2020-04-20
 */
public class RemoteHelloImpl implements RemoteHello {

    @Override
    public String sayHello(String name) throws RemoteException {
        System.out.println("param: " + name);
        return String.format("hello, %s!", name);
    }
}