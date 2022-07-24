package icu.shishc;

import icu.shishc.consts.ZkConstants;
import icu.shishc.zookeeper.ZkConnection;
import org.apache.zookeeper.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ZkApplication {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        SpringApplication.run(ZkApplication.class, args);

        ZkConnection connection = new ZkConnection();
        ZooKeeper zooKeeper = connection.connect();
        // 注册 -> 存储 -> 通知 -> 回调. 当/${zkPath}/create_node/* 任意子目录发生变化，则会回调watcher.
        zooKeeper.addWatch("/" + ZkConstants.zkPath + "/create_node", event -> {
            System.out.println("something happen");
        }, AddWatchMode.PERSISTENT_RECURSIVE);
    }

}
