package com.kvlt.usage.origin;

import com.kvlt.GlobalConfig;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * Zk_AuthSample_Get
 *
 * @author KVLT
 * @date 2019-05-11.
 */
public class Zk_AuthSample_Get {

    final static String PATH = "/zk-book-auth_test";

    public static void main(String[] args) throws Exception {

        ZooKeeper zookeeper1 = new ZooKeeper(GlobalConfig.ZK_IP_PORT, 5000, null);
        zookeeper1.addAuthInfo("digest", "foo:true".getBytes());
        zookeeper1.create(PATH, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
        System.out.println("success create znode: " + PATH);
        ZooKeeper zookeeper2 = new ZooKeeper(GlobalConfig.ZK_IP_PORT, 5000, null);
        zookeeper2.getData(PATH, false, null);
    }

}
