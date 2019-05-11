package com.kvlt.usage.origin;

import com.kvlt.GlobalConfig;
import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

import java.util.concurrent.CountDownLatch;

/**
 * Zk_Delete_API_ASync_Usage
 *
 * @author KVLT
 * @date 2019-05-11.
 */
public class Zk_Delete_API_ASync_Usage implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception {
        String path = "/zk-book";
        zk = new ZooKeeper(GlobalConfig.ZK_IP_PORT, 5000,
                new Zk_Delete_API_ASync_Usage());
        connectedSemaphore.await();

        zk.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("success create znode: " + path);
        zk.create(path + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("success create znode: " + path + "/c1");

        zk.delete(path, -1, new IVoidCallback(), null);
        zk.delete(path + "/c1", -1, new IVoidCallback(), null);
        zk.delete(path, -1, new IVoidCallback(), null);

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event) {
        if (KeeperState.SyncConnected == event.getState()) {
            if (EventType.None == event.getType() && null == event.getPath()) {
                connectedSemaphore.countDown();
            }
        }
    }
}

class IVoidCallback implements AsyncCallback.VoidCallback {
    public void processResult(int rc, String path, Object ctx) {
        System.out.println(rc + ", " + path + ", " + ctx);
    }
}
