package com.kvlt.usage.origin;

import com.kvlt.GlobalConfig;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * Zk_Exist_API_Sync_Usage
 *
 * @author KVLT
 * @date 2019-05-11.
 */
public class Zk_Exist_API_Sync_Usage implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception {
        String path = "/zk-book";
        zk = new ZooKeeper(GlobalConfig.ZK_IP_PORT, 5000, //
                new Zk_Exist_API_Sync_Usage());
        connectedSemaphore.await();

        zk.exists(path, true);

        zk.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.setData(path, "123".getBytes(), -1);

        zk.create(path + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("success create znode: " + path + "/c1");

        zk.delete(path + "/c1", -1);
        zk.delete(path, -1);

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event) {
        try {
            if (KeeperState.SyncConnected == event.getState()) {
                if (EventType.None == event.getType() && null == event.getPath()) {
                    connectedSemaphore.countDown();
                } else if (EventType.NodeCreated == event.getType()) {
                    System.out.println("success create znode: " + event.getPath());
                    zk.exists(event.getPath(), true);
                } else if (EventType.NodeDeleted == event.getType()) {
                    System.out.println("success delete znode: " + event.getPath());
                    zk.exists(event.getPath(), true);
                } else if (EventType.NodeDataChanged == event.getType()) {
                    System.out.println("data changed of znode: " + event.getPath());
                    zk.exists(event.getPath(), true);
                }
            }
        } catch (Exception e) {
        }
    }
}
