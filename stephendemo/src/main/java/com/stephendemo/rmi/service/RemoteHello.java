package com.stephendemo.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author jmfen
 * date 2020-04-20
 */
public interface RemoteHello extends Remote {
    String sayHello(String name) throws RemoteException;
}