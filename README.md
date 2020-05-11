# iFile
 Distributed file system based on Cassandra
 
### 项目结构
  
iFile-Common 公共模块 
iFile-Server Eureka 7001
iFile-Service-File 文件模块 8002
iFile-Service-User 用户模块 8001
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
```sql
CREATE KEYSPACE iFile WITH replication = 
{'class':'SimpleStrategy', 'replication_factor' : 3};

use iFile;

CREATE TABLE user_log(
    username text,
    action text,
    createtime date,
    ip text,
    PRIMARY KEY ( (username),createtime)
)
```


### Redis集群的配置
https://www.jianshu.com/p/87ff94358074
https://blog.csdn.net/admans/article/details/99630511

