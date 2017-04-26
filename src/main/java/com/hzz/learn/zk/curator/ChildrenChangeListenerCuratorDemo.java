package com.hzz.learn.zk.curator;

import com.google.common.base.Joiner;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryUntilElapsed;

import java.util.List;

public class ChildrenChangeListenerCuratorDemo {
    public static void main(String[] args) throws Exception {
        String zkHost = "dev:2181";
        RetryPolicy retryPolicy = new RetryUntilElapsed(100000, 5000);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkHost, retryPolicy);
        client.start();
        client.blockUntilConnected();
//        client.
        PathChildrenCache watcher = new PathChildrenCache(client, "/hzz/test", true);
        watcher.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                List<String> children = curatorFramework.getChildren().forPath("/hzz/test");
                System.out.println("children is : " + Joiner.on(",").join(children));
                System.out.println(pathChildrenCacheEvent.getData() + "type: " + pathChildrenCacheEvent.getType());
            }
        });
        watcher.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        //client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/hzz/test");
        Thread.sleep(300000L);
        client.close();
    }
}
