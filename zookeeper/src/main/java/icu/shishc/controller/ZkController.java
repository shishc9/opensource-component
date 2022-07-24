package icu.shishc.controller;

import icu.shishc.consts.ZkConstants;
import icu.shishc.zookeeper.ZkConnection;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@RestController
public class ZkController {
    private final ZooKeeper zk;

    ZkController() throws IOException, InterruptedException, KeeperException {
        ZkConnection zkConnection = new ZkConnection();
        zk = zkConnection.connect();
    }

    /**
     * 创建 /path/node(data).
     * path以'/'开始，但不以'/'结尾！解析path中若含有多个'/'，最后一个'/'后的内容便是node名称，node的值以data初始化
     * @param path eg. /create_node => the real path: /Client@Client_172.0.0.1/create_node
     */
    @PostMapping("/create")
    public String create(@RequestParam String path, @RequestParam String data) throws InterruptedException, KeeperException {
        path = updatePath(path);
        return zk.create(path, data.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * path格式要求与上述相同。
     * 最后一个'/'后的内容为node名称，若node下还有其他节点，则该path删除失败。
     */
    @DeleteMapping("/delete")
    public void delete(@RequestParam String path) throws InterruptedException, KeeperException {
        path = updatePath(path);
        zk.delete(path, zk.exists(path, true).getVersion());
    }

    /**
     * path若不存在 则返回null
     * 若path存在，则返回以最后一个'/'后的内容为节点的信息 stat
     */
    @GetMapping("/exist")
    public Stat ZNodeExist(@RequestParam String path) throws InterruptedException, KeeperException {
        path = updatePath(path);
        System.out.println(path);
        return zk.exists(path, true);
    }

    /**
     * 获取当前节点里的子路径
     */
    @GetMapping("/child")
    public List<String> getChildren(@RequestParam String path) throws InterruptedException, KeeperException {
        path = updatePath(path);
        System.out.println(path);
        return zk.getChildren(path, false);
    }

    /**
     * 若path不存在会报错，若存在则返回该node的data.
     * 区别于exist，exist返回state node. get返回get node
     */
    @GetMapping("/data")
    public String getData(@RequestParam String path) throws InterruptedException, KeeperException {
        path = updatePath(path);
        System.out.println(path);
        return Arrays.toString(zk.getData(path, true, null));
    }

    /**
     * 若path不存在则报错，若存在直接修改node值
     */
    @PostMapping("/data")
    public void setData(@RequestParam String path, @RequestParam String data) throws InterruptedException, KeeperException {
        path = updatePath(path);
        System.out.println(path);
        zk.setData(path, data.getBytes(StandardCharsets.UTF_8), zk.exists(path, true).getVersion());
    }

    private String updatePath(String path) {
        // path: /Client@Client_172.0.0.1
        return "/" + ZkConstants.zkPath + path;
    }

}
