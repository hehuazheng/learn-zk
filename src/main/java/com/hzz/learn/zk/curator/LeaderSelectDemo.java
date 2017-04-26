package com.hzz.learn.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.EnsurePath;

public class LeaderSelectDemo {
    public static void main(String[] args) throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("ended");
            }
        });
        final LeaderSelectorListener listener = new LeaderSelectorListener() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println(Thread.currentThread().getName() + " take leader ship");
                Thread.sleep(50000L);
                System.out.println(Thread.currentThread().getName() + " re leadership");
            }

            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
            }
        };
        new Thread("t2") {
            @Override
            public void run() {
                registerListener(listener);
            }
        }.start();
//        new Thread("t2") {
//            @Override
//            public void run() {
//                registerListener(listener);
//            }
//        }.start();
        Thread.sleep(300000L);
    }

    static class NamedThread extends  Thread {
        private String name;
        public NamedThread(String name) {
            this.name = name;
        }
    }

    static String zkPath = "/hzz/locks/lock1";

    static void registerListener(LeaderSelectorListener listener) {
        CuratorFramework client = CuratorFrameworkFactory.newClient("dev:2181", new RetryNTimes(10, 5000));
        client.start();
        try {
            new EnsurePath(zkPath).ensure(client.getZookeeperClient());
        } catch (Exception e) {
            e.printStackTrace();
        }
        LeaderSelector selector = new LeaderSelector(client, zkPath, listener);
        selector.autoRequeue();
        selector.start();
    }
}
