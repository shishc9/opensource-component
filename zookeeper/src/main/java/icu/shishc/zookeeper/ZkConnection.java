package icu.shishc.zookeeper;

import icu.shishc.consts.ZkConstants;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class ZkConnection {

    private ZooKeeper zk;
    final CountDownLatch connectSignal = new CountDownLatch(1);

    public ZooKeeper connect() throws IOException, InterruptedException, KeeperException {
        zk = new ZooKeeper(ZkConstants.hostname, 5000, watchedEvent -> {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                connectSignal.countDown();
            }
        });

        connectSignal.await();
        System.out.println("zk connect.");
        System.out.println(Arrays.toString(zk.getData("/" + ZkConstants.zkPath, false, null)));
        if (zk.getData("/" + ZkConstants.zkPath, false, null) == null) {
            zk.create("/" + ZkConstants.zkPath, "shishc_node".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        return zk;
    }

    public void close() throws InterruptedException {
        zk.close();
    }

}
