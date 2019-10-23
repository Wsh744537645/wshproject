package com.zk.zkSession;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * 创建节点，使用同步(sync)接口
 * @Author: stephen
 * @Date: 2019/7/12 14:57
 */

public class Zk_Create_API_Sync_Usage implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("192.168.0.203:2181", 5000, new Zk_Create_API_Sync_Usage());
        connectedSemaphore.await();

        String path1 = zooKeeper.create("/zk-test-ephemeral-", "hehheda".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Success create znode: " + path1);

        String path2 = zooKeeper.create("/zk-test-ephemeral-", "memeda".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("Success create zonde: " + path2);

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            connectedSemaphore.countDown();
        }
    }
}
