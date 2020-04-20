package com.stephendemo.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.CountDownLatch;

/**
 * @author jmfen
 * date 2020-04-20
 */
public class RegistryServer {

    public static void main(String[] args) throws InterruptedException {

        try {
            LocateRegistry.createRegistry(8000);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}