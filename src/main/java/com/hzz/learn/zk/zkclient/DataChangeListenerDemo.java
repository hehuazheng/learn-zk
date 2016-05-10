package com.hzz.learn.zk.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * 防止脑裂leader选举
 */
public class DataChangeListenerDemo {

	public static void main(String[] args) throws InterruptedException {
		final ZkClient zkClient = new ZkClient("zkserver1:2181");
		String rootPath = "/learn-zk";
		if (!zkClient.exists(rootPath)) {
			zkClient.create(rootPath, "", CreateMode.PERSISTENT);
		}
		final String testPath = rootPath + "/testAcl";
		zkClient.createEphemeral(testPath);
		new Thread() {
			public void run() {
				try {
					Thread.sleep(3000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("try to change data...");
				// wait 3 seconds to change the data
				zkClient.writeData(testPath, "changed data");
				System.out.println("data changed successfully");
			};
		}.start();

		final Holder holder = new Holder(Boolean.FALSE);
		zkClient.subscribeDataChanges(testPath, new IZkDataListener() {

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println(dataPath + " changed ");
			}

			@Override
			public void handleDataChange(String dataPath, Object data)
					throws Exception {
				System.out.println(dataPath + " changed to " + data);
				holder.setObject(Boolean.TRUE);
			}
		});
		while (!(Boolean) holder.getObject()) {
			Thread.sleep(500L);
		}
		zkClient.close();
	}

	static class Holder {
		private Object obj;

		public Holder(Object obj) {
			this.obj = obj;
		}

		public void setObject(Boolean obj) {
			this.obj = obj;
		}

		public Object getObject() {
			return obj;
		}
	}

}
