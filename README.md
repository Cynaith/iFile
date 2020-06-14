# iFile
 Distributed file system based on Cassandra
 
### 项目结构
  
iFile-Common 公共模块
<br/>
iFile-Server Eureka 7001
<br/>
iFile-Service-File 文件模块 8002
<br/>
iFile-Service-User 用户模块 8001
<br/>
iFile-Zuul 网关模块 8000
 
 
### CassAndra的安装 
 - 将Cassandra的Apache存储库添加到/etc/apt/sources.list.d/cassandra.sources.list，例如，以获取最新的3.11版本：<br/>
 `echo "deb https://downloads.apache.org/cassandra/debian 311x main" | sudo tee -a /etc/apt/sources.list.d/cassandra.sources.list`
- 添加Apache Cassandra存储库密钥：<br/>
`curl https://downloads.apache.org/cassandra/KEYS | sudo apt-key add -`
- 更新存储库：<br/>
`sudo apt-get update`
- 安装Cassandra：<br/>
`sudo apt-get install cassandra`
> 您可以使用启动Cassandra sudo service cassandra start并使用停止它sudo service cassandra stop。但是通常该服务将自动启动。
> 因此，如果需要进行任何配置更改，请确保将其停止。<br/>
> 通过nodetool status从命令行调用来验证Cassandra是否正在运行。<br/>
> 配置文件的默认位置是/etc/cassandra。<br/>
> 日志和数据目录的默认位置是/var/log/cassandra/和/var/lib/cassandra。<br/>
> 可以在中配置启动选项（堆大小等）/etc/default/cassandra。<br/>

### CQL
```cassandraql
CREATE KEYSPACE iFile WITH replication =
{'class':'SimpleStrategy', 'replication_factor' : 3};

use iFile;

CREATE TABLE user_log(
    username TEXT,
    action TEXT,
    createtime TIMESTAMP,
    ip TEXT,
    PRIMARY KEY ((username),createtime)
);

CREATE TABLE file(
    username TEXT,
    filetype TEXT,
    createtime TIMESTAMP,
    fileid BIGINT,
    filename TEXT,
    filesuffix TEXT,
    PRIMARY KEY (( username ),createtime)
);

CREATE TABLE user(
    username TEXT,
    password TEXT,
    kind TEXT,
    PRIMARY KEY ( kind )
);

CREATE TABLE filedata(
    fileid BIGINT,
    filedata BLOB,
    fileno BIGINT,
    PRIMARY KEY ( (fileid),fileno )
);
TRUNCATE ifile.filedata;
TRUNCATE iFile.file;
```

### CassandraTemplate批处理  
使用`template.batchOps()`开启批处理。

### 文件存储  
Cassandra单独存储大文件会读取超时，所以应将文件分段存储，并将文件表拆分。

### 为什么使用Cassandra做分布式缓存  
对字段进行一致性Hash，优化Query效率  
CAP满足可用性和分区容忍性，满足最终一致性，对于insert少的系统而言是可以接受的。

### Redis集群的配置
https://www.jianshu.com/p/87ff94358074
<br/>
https://blog.csdn.net/admans/article/details/99630511

### Zuul
- [解决浏览器Cookie禁用](https://blog.csdn.net/u014508939/article/details/78678790?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-4.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-4.nonecase)
- 判断是否屏蔽cookie
    - name为“JSESSIONID”的cookie
- Redis-Hash 存储 Session数据
    - Hash K 记录用户登陆信息
        - SessionID 会话id
        > 同一ip不同SessionID，将重命名SessionID
    - Hash V
        - V key
            - 用户Session K
        - V value
            - 用户Session V
        > username token 
- Redis-Set 存储全局 Session
    - Set K
        - ${系统名}
    - Set Vp
        - V key
            - ip 登陆ip
        - V value
            - SessionID 会话id
