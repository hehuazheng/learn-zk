package com.hzz.learn.zk.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * 测试leader选举
 */
public class LeaderSelectDemo {
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("zkserver1:2181");
		String rootPath = "/learn-zk";
		if (!zkClient.exists(rootPath)) {
			zkClient.create(rootPath, "", CreateMode.PERSISTENT);
		}
		// first success
		zkClient.create(rootPath + "/master", "", CreateMode.EPHEMERAL);
		// second failure
		zkClient.create(rootPath + "/master", "", CreateMode.EPHEMERAL);
		zkClient.close();
	}

}
