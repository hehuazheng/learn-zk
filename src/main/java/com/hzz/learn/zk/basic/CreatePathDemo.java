package com.hzz.learn.zk.basic;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;

public class CreatePathDemo {

	public static void main(String[] args) throws IOException, KeeperException,
			InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		Watcher watcher = new ConnectedWatcher(countDownLatch);
		ZooKeeper zk = new ZooKeeper("10.185.240.141:2181", 5, watcher);
		if(States.CONNECTING == zk.getState()) {
			countDownLatch.await();
		}
		// while (States.CONNECTING == zk.getState()) {
		// Thread.sleep(500L);
		// }
		zk.create("/learn-zk/createPath", "".getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
		zk.close();
	}

	static class ConnectedWatcher implements Watcher {
		private CountDownLatch countDownLatch;

		public ConnectedWatcher(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void process(WatchedEvent event) {
			if (event.getState() == KeeperState.SyncConnected) {
				countDownLatch.countDown();
			}
		}

	}
}
