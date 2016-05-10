package com.hzz.learn.zk.zkclient;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

public class AclDemo {
	public static void main(String[] args) throws InterruptedException,
			NoSuchAlgorithmException {
		ZkClient zkClient = new ZkClient("zkserver1:2181");
		String rootPath = "/learn-zk";
		if (!zkClient.exists(rootPath)) {
			zkClient.create(rootPath, "", CreateMode.PERSISTENT);
		}
		// first success
		final String path = zkClient.create(rootPath + "/master", "i'm master",
				CreateMode.EPHEMERAL);
		List<ACL> list = new ArrayList<ACL>();
		ACL acl = new ACL(ZooDefs.Perms.ALL, new Id("digest",
				DigestAuthenticationProvider.generateDigest("user:password")));
		list.add(acl);
		zkClient.setAcl(path, list);

		new Thread() {
			public void run() {
				// not authorized
				System.out
						.println("start another thread to read data without acl settings");
				ZkClient newZkClient = new ZkClient("zkserver1:2181");
				try {
					String data = newZkClient.readData(path, true);
					System.out.println("t1 data is : " + data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
		new Thread() {
			public void run() {
				System.out
						.println("start another thread to read data with acl settings");
				ZkClient newZkClient = new ZkClient("zkserver1:2181");
				// => auth info added
				newZkClient.addAuthInfo("digest", "user:password".getBytes());
				List<ACL> aclList = new ArrayList<ACL>();
				try {
					ACL acl = new ACL(ZooDefs.Perms.ALL, new Id("digest",
							DigestAuthenticationProvider
									.generateDigest("user:password")));
					aclList.add(acl);
					newZkClient.setAcl(path, aclList);
					String data = newZkClient.readData(path, true);
					System.out.println("t2 data is : " + data);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			};
		}.start();
		Thread.sleep(5000L);
		zkClient.close();
	}
}
