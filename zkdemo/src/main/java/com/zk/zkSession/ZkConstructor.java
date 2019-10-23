package com.zk.zkSession;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * 创建一个最基本的Zookeeper会话实例
 * @Author: stephen
 * @Date: 2019/7/12 14:22
 */
public class ZkConstructor implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("192.168.0.203:2181", 5000, new ZkConstructor());
        System.out.println("state = " + zooKeeper.getState());

        try{
            connectedSemaphore.await();
        }catch (InterruptedException e){

        }
        System.out.println("Zookeeper session established.");

        //复用sessionId和sessionPasswd来创建一个Zookeeper对象实例
        long sessionId = zooKeeper.getSessionId();
        byte[] passwd = zooKeeper.getSessionPasswd();

        zooKeeper = new ZooKeeper("192.168.0.203:2181", 5000, new ZkConstructor(), sessionId, passwd);
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event: " + watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            connectedSemaphore.countDown();
        }
    }
}
