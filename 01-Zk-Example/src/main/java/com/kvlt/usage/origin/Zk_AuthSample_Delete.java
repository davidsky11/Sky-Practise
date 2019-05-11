package com.kvlt.usage.origin;

import com.kvlt.GlobalConfig;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * Zk_AuthSample_Delete
 *
 * @author KVLT
 * @date 2019-05-11.
 */
public class Zk_AuthSample_Delete {

    final static String PATH = "/zk-book-auth_test";
    final static String PATH2 = "/zk-book-auth_test/child";

    public static void main(String[] args) throws Exception {
        ZooKeeper zookeeper1 = new ZooKeeper(GlobalConfig.ZK_IP_PORT, 5000, null);
        zookeeper1.addAuthInfo("digest", "foo:true".getBytes());
        zookeeper1.create(PATH, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);

        zookeeper1.create(PATH2, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        try {
            ZooKeeper zookeeper2 = new ZooKeeper(GlobalConfig.ZK_IP_PORT, 5000, null);
            zookeeper2.delete(PATH2, -1);
        } catch (Exception e) {
            System.out.println("fail to delete: " + e.getMessage());
        }

        ZooKeeper zookeeper3 = new ZooKeeper(GlobalConfig.ZK_IP_PORT, 5000, null);
        zookeeper3.addAuthInfo("digest", "foo:true".getBytes());
        zookeeper3.delete(PATH2, -1);
        System.out.println("success delete znode: " + PATH2);

        ZooKeeper zookeeper4 = new ZooKeeper(GlobalConfig.ZK_IP_PORT, 5000, null);
        zookeeper4.delete(PATH, -1);
        System.out.println("success delete znode: " + PATH);
    }

}
