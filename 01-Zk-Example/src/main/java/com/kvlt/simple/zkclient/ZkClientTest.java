package com.kvlt.simple.zkclient;

import com.kvlt.GlobalConfig;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * ZkClientTest
 *
 * @author KVLT
 * @date 2019-05-11.
 */
public class ZkClientTest {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(GlobalConfig.ZK_IP_PORT);
        zkClient.create("/root", "mydata", CreateMode.PERSISTENT);  // 创建目录并写入数据
        String data = zkClient.readData("/root");
        System.out.println(data);

//        zkClient.delete("/root");  // 删除目录
//        zkClient.deleteRecursive("/root");  // 递归删除
    }
}
