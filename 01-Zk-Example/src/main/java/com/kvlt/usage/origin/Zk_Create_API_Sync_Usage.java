package com.kvlt.usage.origin;

import com.kvlt.GlobalConfig;
import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * Zk_Create_API_Sync_Usage
 *
 * @author KVLT
 * @date 2019-05-11.
 */
public class Zk_Create_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zookeeper = new ZooKeeper(GlobalConfig.ZK_IP_PORT, 5000, new Zk_Create_API_Sync_Usage());
        System.out.println(zookeeper.getState());
        connectedSemaphore.await();

        String path1 = zookeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Success create znode: " + path1);

        String path2 = zookeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("Success create znode: " + path2);
    }

    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }
    }

}
