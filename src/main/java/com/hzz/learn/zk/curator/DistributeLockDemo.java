package com.hzz.learn.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.TimeUnit;

public class DistributeLockDemo {
    static String zkPath = "/hzz/distributeLocks/lock1";
    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("dev:2181", new RetryNTimes(10, 5000));
        client.start();
        InterProcessMutex mutex = new InterProcessMutex(client,zkPath );
        if(mutex.acquire(10, TimeUnit.SECONDS)) {
            System.out.println("lock acquired");
            mutex.release();
        }
        client.close();
    }
}
