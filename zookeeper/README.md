## Zookeeper

### Install zookeeper in Centos7

```bash
$ wget https://mirrors.bfsu.edu.cn/apache/zookeeper/zookeeper-3.8.0/apache-zookeeper-3.8.0-bin.tar.gz
# 将该安装包移动至 /usr/local/ 下 （任意目录均可）
$ tar -zxvf apache-zookeeper-3.8.0-bin.tar.gz

# 配置环境变量
$ vi /etc/profile

# 在末尾加入以下内容---
export ZOOKEEPER_HOME=/usr/local/apache-zookeeper-3.8.0-bin
export PATH=$ZOOKEEPER_HOME/bin:$PATH
#                 ---

$ source /etc/profile
```

安装完成之后，测试安装是否成功
```bash
cd /usr/local/apache-zookeeper-3.8.0-bin/bin

sh zkCli.sh
```

### how to use

修改 `/src/main/java/icu/shishc/consts/ZkConstants.java` 中 `hostname` 变量为安装zk的实例ip即可

其余功能以spring boot框架标准食用即可
