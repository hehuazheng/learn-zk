package com.hzz.learn.zk.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

public class CreatePathDemo {
	/**
	 * create方法不能一次创建多重path
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("zkserver1:2181");
		zkClient.create("/learn-zk", "", CreateMode.EPHEMERAL);
		zkClient.close();
	}

}
