# JAVA 语言 MySQL Binlog 处理

binlog 测试数据 统一如下

binlog.000001 157-1624

```shell
mysql> reset master;
Query OK, 0 rows affected (0.01 sec)

mysql> show master status;
+---------------+----------+--------------+------------------+-------------------+
| File          | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+---------------+----------+--------------+------------------+-------------------+
| binlog.000001 |      157 |              |                  |                   |
+---------------+----------+--------------+------------------+-------------------+
1 row in set (0.00 sec)

mysql> create database school;
Query OK, 1 row affected (0.00 sec)

mysql> use school;
Database changed
mysql> create table students (student_id int auto_increment, name varchar(32), age int, primary key (student_id));
Query OK, 0 rows affected (0.01 sec)

mysql> insert into students values (0, 'zhangsan', 10), (0, 'lisi', 20);
Query OK, 2 rows affected (0.02 sec)
Records: 2  Duplicates: 0  Warnings: 0

mysql> update students set age=18 where student_id in (1, 2);
Query OK, 2 rows affected (0.01 sec)
Rows matched: 2  Changed: 2  Warnings: 0

mysql> delete from students ^Cere student_id in (1, 2);
mysql> select * from students;
+------------+----------+------+
| student_id | name     | age  |
+------------+----------+------+
|          1 | zhangsan |   18 |
|          2 | lisi     |   18 |
+------------+----------+------+
2 rows in set (0.00 sec)

mysql> delete from students where student_id in (1, 2);
Query OK, 2 rows affected (0.01 sec)

mysql> show master status;
+---------------+----------+--------------+------------------+-------------------+
| File          | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+---------------+----------+--------------+------------------+-------------------+
| binlog.000001 |     1624 |              |                  |                   |
+---------------+----------+--------------+------------------+-------------------+
1 row in set (0.00 sec)
```

## mysql-binlog-connector-java

https://github.com/shyiko/mysql-binlog-connector-java  
https://github.com/osheroff/mysql-binlog-connector-java

列名需要自己去mysql查，不支持分布式，存在单点故障，需要自己避免单点故障

```xml
<dependency>
    <groupId>com.zendesk</groupId>
    <artifactId>mysql-binlog-connector-java</artifactId>
    <version>0.25.0</version>
</dependency>
```

调试结果

```text
Jun 06, 2023 2:07:41 PM com.github.shyiko.mysql.binlog.BinaryLogClient connect
信息: Connected to 192.168.1.205:3306 at binlog.000001/157 (sid:65535, cid:10)
Event{header=EventHeaderV4{timestamp=0, eventType=ROTATE, serverId=1, headerLength=19, dataLength=25, nextPosition=0, flags=32}, data=RotateEventData{binlogFilename='binlog.000001', binlogPosition=157}}
Event{header=EventHeaderV4{timestamp=1686031301000, eventType=FORMAT_DESCRIPTION, serverId=1, headerLength=19, dataLength=103, nextPosition=0, flags=0}, data=FormatDescriptionEventData{binlogVersion=4, serverVersion='8.0.33', headerLength=19, dataLength=98, checksumType=CRC32}}
Event{header=EventHeaderV4{timestamp=1686031313000, eventType=ANONYMOUS_GTID, serverId=1, headerLength=19, dataLength=58, nextPosition=234, flags=0}, data=null}
Event{header=EventHeaderV4{timestamp=1686031313000, eventType=QUERY, serverId=1, headerLength=19, dataLength=95, nextPosition=348, flags=8}, data=QueryEventData{threadId=9, executionTime=0, errorCode=0, database='school', sql='create database school'}}
Event{header=EventHeaderV4{timestamp=1686031380000, eventType=ANONYMOUS_GTID, serverId=1, headerLength=19, dataLength=60, nextPosition=427, flags=0}, data=null}
Event{header=EventHeaderV4{timestamp=1686031380000, eventType=QUERY, serverId=1, headerLength=19, dataLength=179, nextPosition=625, flags=0}, data=QueryEventData{threadId=9, executionTime=0, errorCode=0, database='school', sql='create table students (student_id int auto_increment, name varchar(32), age int, primary key (student_id))'}}
Event{header=EventHeaderV4{timestamp=1686031417000, eventType=ANONYMOUS_GTID, serverId=1, headerLength=19, dataLength=60, nextPosition=704, flags=0}, data=null}
Event{header=EventHeaderV4{timestamp=1686031417000, eventType=QUERY, serverId=1, headerLength=19, dataLength=58, nextPosition=781, flags=8}, data=QueryEventData{threadId=9, executionTime=0, errorCode=0, database='school', sql='BEGIN'}}
Event{header=EventHeaderV4{timestamp=1686031417000, eventType=TABLE_MAP, serverId=1, headerLength=19, dataLength=46, nextPosition=846, flags=0}, data=TableMapEventData{tableId=113, database='school', table='students', columnTypes=3, 15, 3, columnMetadata=0, 128, 0, columnNullability={1, 2}, eventMetadata=TableMapEventMetadata{signedness={}, defaultCharset=255, charsetCollations=null, columnCharsets=null, columnNames=null, setStrValues=null, enumStrValues=null, geometryTypes=null, simplePrimaryKeys=null, primaryKeysWithPrefix=null, enumAndSetDefaultCharset=null, enumAndSetColumnCharsets=null}}}
Event{header=EventHeaderV4{timestamp=1686031417000, eventType=EXT_WRITE_ROWS, serverId=1, headerLength=19, dataLength=48, nextPosition=913, flags=0}, data=WriteRowsEventData{tableId=113, includedColumns={0, 1, 2}, rows=[
    [1, [B@21b8d17c, 10],
    [2, [B@6433a2, 20]
]}}
Event{header=EventHeaderV4{timestamp=1686031417000, eventType=XID, serverId=1, headerLength=19, dataLength=12, nextPosition=944, flags=0}, data=XidEventData{xid=15}}
Event{header=EventHeaderV4{timestamp=1686031451000, eventType=ANONYMOUS_GTID, serverId=1, headerLength=19, dataLength=60, nextPosition=1023, flags=0}, data=null}
Event{header=EventHeaderV4{timestamp=1686031451000, eventType=QUERY, serverId=1, headerLength=19, dataLength=67, nextPosition=1109, flags=8}, data=QueryEventData{threadId=9, executionTime=0, errorCode=0, database='school', sql='BEGIN'}}
Event{header=EventHeaderV4{timestamp=1686031451000, eventType=TABLE_MAP, serverId=1, headerLength=19, dataLength=46, nextPosition=1174, flags=0}, data=TableMapEventData{tableId=113, database='school', table='students', columnTypes=3, 15, 3, columnMetadata=0, 128, 0, columnNullability={1, 2}, eventMetadata=TableMapEventMetadata{signedness={}, defaultCharset=255, charsetCollations=null, columnCharsets=null, columnNames=null, setStrValues=null, enumStrValues=null, geometryTypes=null, simplePrimaryKeys=null, primaryKeysWithPrefix=null, enumAndSetDefaultCharset=null, enumAndSetColumnCharsets=null}}}
Event{header=EventHeaderV4{timestamp=1686031451000, eventType=EXT_UPDATE_ROWS, serverId=1, headerLength=19, dataLength=81, nextPosition=1274, flags=0}, data=UpdateRowsEventData{tableId=113, includedColumnsBeforeUpdate={0, 1, 2}, includedColumns={0, 1, 2}, rows=[
    {before=[1, [B@7a07c5b4, 10], after=[1, [B@26a1ab54, 18]},
    {before=[2, [B@3d646c37, 20], after=[2, [B@41cf53f9, 18]}
]}}
Event{header=EventHeaderV4{timestamp=1686031451000, eventType=XID, serverId=1, headerLength=19, dataLength=12, nextPosition=1305, flags=0}, data=XidEventData{xid=17}}
Event{header=EventHeaderV4{timestamp=1686031495000, eventType=ANONYMOUS_GTID, serverId=1, headerLength=19, dataLength=60, nextPosition=1384, flags=0}, data=null}
Event{header=EventHeaderV4{timestamp=1686031495000, eventType=QUERY, serverId=1, headerLength=19, dataLength=58, nextPosition=1461, flags=8}, data=QueryEventData{threadId=9, executionTime=0, errorCode=0, database='school', sql='BEGIN'}}
Event{header=EventHeaderV4{timestamp=1686031495000, eventType=TABLE_MAP, serverId=1, headerLength=19, dataLength=46, nextPosition=1526, flags=0}, data=TableMapEventData{tableId=113, database='school', table='students', columnTypes=3, 15, 3, columnMetadata=0, 128, 0, columnNullability={1, 2}, eventMetadata=TableMapEventMetadata{signedness={}, defaultCharset=255, charsetCollations=null, columnCharsets=null, columnNames=null, setStrValues=null, enumStrValues=null, geometryTypes=null, simplePrimaryKeys=null, primaryKeysWithPrefix=null, enumAndSetDefaultCharset=null, enumAndSetColumnCharsets=null}}}
Event{header=EventHeaderV4{timestamp=1686031495000, eventType=EXT_DELETE_ROWS, serverId=1, headerLength=19, dataLength=48, nextPosition=1593, flags=0}, data=DeleteRowsEventData{tableId=113, includedColumns={0, 1, 2}, rows=[
    [1, [B@306a30c7, 18],
    [2, [B@b81eda8, 18]
]}}
Event{header=EventHeaderV4{timestamp=1686031495000, eventType=XID, serverId=1, headerLength=19, dataLength=12, nextPosition=1624, flags=0}, data=XidEventData{xid=20}}
```

## binlogportal

https://github.com/dothetrick/binlogportal

可单机，可分布式高可用，依赖redis。列名已提供，非常方便。通过修改redis数据可修改binlog文件和位置

```text
分布式部署实现
项目中高可用实现是基于redis的分布式锁。
每个实例都会加载全部数据库的配置，在创建binlog连接之前，先要获取redis锁，获取锁后会定时刷新锁的过期时间。所有实例会定时重新抢锁。
同一个mysql库的binlog文件和position会保存在redis里，如果一个实例宕机。新抢到锁的实例在初始化时，会使用上个实例已保存的binlog信息继续获取。
```

portal /ˈpɔː.təl/ 门户网页；门户网站  
portals （建筑物的）大门，正门

```xml
<dependency>
  <groupId>com.insistingon.binlogportal</groupId>
  <artifactId>binlogportal</artifactId>
  <version>1.0.5</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>1.7.36</version>
</dependency>
```

```shell
127.0.0.1:6379> keys *
1) "192.168.1.205:3306"
2) "YdV.lLak$aSkzHN/KJXI2bi4IrHX.V."
127.0.0.1:6379> get 192.168.1.205:3306
"{\"binlogName\":\"binlog.000001\",\"position\":1593,\"serverId\":1}"
127.0.0.1:6379> set 192.168.1.205:3306 "{\"binlogName\":\"binlog.000001\",\"position\":157,\"serverId\":1}"
OK
```

调试输出

```text
[main] INFO org.redisson.Version - Redisson 3.13.3
[redisson-netty-2-13] INFO org.redisson.connection.pool.MasterPubSubConnectionPool - 1 connections initialized for /192.168.1.205:6379
[redisson-netty-2-19] INFO org.redisson.connection.pool.MasterConnectionPool - 24 connections initialized for /192.168.1.205:6379
Jun 06, 2023 3:14:30 PM com.github.shyiko.mysql.binlog.BinaryLogClient openChannelToBinaryLogStream
信息: Connected to 192.168.1.205:3306 at binlog.000001/157 (sid:65535, cid:57)
EventEntity{event=Event{header=EventHeaderV4{timestamp=1686031417000, eventType=EXT_WRITE_ROWS, serverId=1, headerLength=19, dataLength=48, nextPosition=913, flags=0}, data=WriteRowsEventData{tableId=113, includedColumns={0, 1, 2}, rows=[
    [1, [B@492c9622, 10],
    [2, [B@7dc0fe0e, 20]
]}}, eventEntityType=INSERT, databaseName='school', tableName='students', columns=[com.insistingon.binlogportal.tablemeta.TableMetaEntity$ColumnMetaData@15ac5ce1, com.insistingon.binlogportal.tablemeta.TableMetaEntity$ColumnMetaData@37d33ea3, com.insistingon.binlogportal.tablemeta.TableMetaEntity$ColumnMetaData@1a6323fd], changeBefore=null, changeAfter=[1, zhangsan, 10]}
列名：student_id name age 
EventEntity{event=Event{header=EventHeaderV4{timestamp=1686031417000, eventType=EXT_WRITE_ROWS, serverId=1, headerLength=19, dataLength=48, nextPosition=913, flags=0}, data=WriteRowsEventData{tableId=113, includedColumns={0, 1, 2}, rows=[
    [1, [B@492c9622, 10],
    [2, [B@7dc0fe0e, 20]
]}}, eventEntityType=INSERT, databaseName='school', tableName='students', columns=[com.insistingon.binlogportal.tablemeta.TableMetaEntity$ColumnMetaData@15ac5ce1, com.insistingon.binlogportal.tablemeta.TableMetaEntity$ColumnMetaData@37d33ea3, com.insistingon.binlogportal.tablemeta.TableMetaEntity$ColumnMetaData@1a6323fd], changeBefore=null, changeAfter=[2, lisi, 20]}
列名：student_id name age 
EventEntity{event=Event{header=EventHeaderV4{timestamp=1686031451000, eventType=EXT_UPDATE_ROWS, serverId=1, headerLength=19, dataLength=81, nextPosition=1274, flags=0}, data=UpdateRowsEventData{tableId=113, includedColumnsBeforeUpdate={0, 1, 2}, includedColumns={0, 1, 2}, rows=[
    {before=[1, [B@61cb6a20, 10], after=[1, [B@4a59e5b, 18]},
    {before=[2, [B@13854e65, 20], after=[2, [B@44ce12f6, 18]}
]}}, eventEntityType=UPDATE, databaseName='school', tableName='students', columns=[com.insistingon.binlogportal.tablemeta.TableMetaEntity$ColumnMetaData@15ac5ce1, com.insistingon.binlogportal.tablemeta.TableMetaEntity$ColumnMetaData@37d33ea3, com.insistingon.binlogportal.tablemeta.TableMetaEntity$ColumnMetaData@1a6323fd], changeBefore=[1, zhangsan, 10], changeAfter=[1, zhangsan, 18]}
列名：student_id name age 
EventEntity{event=Event{header=EventHeaderV4{timestamp=1686031451000, eventType=EXT_UPDATE_ROWS, serverId=1, headerLength=19, dataLength=81, nextPosition=1274, flags=0}, data=UpdateRowsEventData{tableId=113, includedColumnsBeforeUpdate={0, 1, 2}, includedColumns={0, 1, 2}, rows=[
    {before=[1, [B@61cb6a20, 10], after=[1, [B@4a59e5b, 18]},
    {before=[2, [B@13854e65, 20], after=[2, [B@44ce12f6, 18]}
]}}, eventEntityType=UPDATE, databaseName='school', tableName='students', columns=[com.insistingon.binlogportal.tablemeta.TableMetaEntity$ColumnMetaData@15ac5ce1, com.insistingon.binlogportal.tablemeta.TableMetaEntity$ColumnMetaData@37d33ea3, com.insistingon.binlogportal.tablemeta.TableMetaEntity$ColumnMetaData@1a6323fd], changeBefore=[2, lisi, 20], changeAfter=[2, lisi, 18]}
列名：student_id name age 
```

## open-replicator

https://github.com/whitesock/open-replicator

貌似很久没人维护了。对于新版本mysql可能不兼容。

```xml
<dependency>
    <groupId>com.zendesk</groupId>
    <artifactId>open-replicator</artifactId>
    <version>1.6.0</version>
</dependency>
```

调试结果

```text
[main] INFO com.google.code.or.OpenReplicator - starting replication at binlog.000001:157
[binlog-parser-1] WARN com.google.code.or.binlog.impl.parser.QueryEventParser - unknown status variable type: 17
[binlog-parser-1] WARN com.google.code.or.binlog.impl.parser.QueryEventParser - unknown status variable type: 17
[binlog-parser-1] WARN com.google.code.or.binlog.impl.parser.QueryEventParser - unknown status variable type: 18
press 'q' to stop
FormatDescriptionEvent[header=BinlogEventV4HeaderImpl[timestamp=1686031301000,eventType=15,serverId=1,eventLength=122,nextPosition=0,flags=0,timestampOfReceipt=1686039210681],binlogVersion=4,serverVersion=8.0.33                                            ,createTimestamp=0,headerLength=19,eventTypes=[0, 13, 0, 8, 0, 0, 0, 0, 4, 0, 4, 0, 0, 0, 98, 0, 4, 26, 8, 0, 0, 0, 8, 8, 8, 2, 0, 0, 0, 10, 10, 10, 42, 42, 0, 18, 52, 0, 10, 40, 0, 1, 125, -72, -10, -26]]
QueryEvent[header=BinlogEventV4HeaderImpl[timestamp=1686031313000,eventType=2,serverId=1,eventLength=114,nextPosition=348,flags=8,timestampOfReceipt=1686039210684],threadId=9,elapsedTime=0,databaseNameLength=6,errorCode=0,statusVariablesLength=49,statusVariables=[QFlags2Code[flags=0], QSQLModeCode[sqlMode=1168113696], QCatalogNzCode[catalogName=std], QCharsetCode[characterSetClient=255,collationConnection=255,collationServer=255], QUpdatedDBNames[accessedDbCount=1,accessedDbs=[Lcom.google.code.or.common.glossary.column.StringColumn;@a3917f]],databaseName=school,sql=create database school0�ï]
QueryEvent[header=BinlogEventV4HeaderImpl[timestamp=1686031380000,eventType=2,serverId=1,eventLength=198,nextPosition=625,flags=0,timestampOfReceipt=1686039210687],threadId=9,elapsedTime=0,databaseNameLength=6,errorCode=0,statusVariablesLength=49,statusVariables=[QFlags2Code[flags=0], QSQLModeCode[sqlMode=1168113696], QCatalogNzCode[catalogName=std], QCharsetCode[characterSetClient=255,collationConnection=255,collationServer=255], QUpdatedDBNames[accessedDbCount=1,accessedDbs=[Lcom.google.code.or.common.glossary.column.StringColumn;@5e700e8f]],databaseName=school,sql=create table students (student_id int auto_increment, name varchar(32), age int, primary key (student_id))53Ę]
QueryEvent[header=BinlogEventV4HeaderImpl[timestamp=1686031417000,eventType=2,serverId=1,eventLength=77,nextPosition=781,flags=8,timestampOfReceipt=1686039210687],threadId=9,elapsedTime=0,databaseNameLength=6,errorCode=0,statusVariablesLength=29,statusVariables=[QFlags2Code[flags=0], QSQLModeCode[sqlMode=1168113696], QCatalogNzCode[catalogName=std], QCharsetCode[characterSetClient=255,collationConnection=255,collationServer=255]],databaseName=school,sql=BEGIN)]
TableMapEvent[header=BinlogEventV4HeaderImpl[timestamp=1686031417000,eventType=19,serverId=1,eventLength=65,nextPosition=846,flags=0,timestampOfReceipt=1686039210687],tableId=113,reserved=1,databaseNameLength=6,databaseName=school,tableNameLength=8,tableName=students,columnCount=3,columnTypes=[3, 15, 3],columnMetadataCount=2,columnMetadata=Metadata[metadata=[0, 128, 0]],columnNullabilities=110]
[binlog-parser-1] ERROR com.google.code.or.binlog.impl.AbstractBinlogParser - failed to parse binlog
java.lang.RuntimeException: assertion failed!  We left 12 unconsumed bytes in the buffer for event: BinlogEventV4HeaderImpl[timestamp=1686031417000,eventType=19,serverId=1,eventLength=65,nextPosition=846,flags=0,timestampOfReceipt=1686039210687]
	at com.google.code.or.net.impl.EventInputStream.finishEvent(EventInputStream.java:73)
	at com.google.code.or.binlog.impl.ReplicationBasedBinlogParser.doParse(ReplicationBasedBinlogParser.java:166)
	at com.google.code.or.binlog.impl.AbstractBinlogParser$Task.run(AbstractBinlogParser.java:263)
	at java.lang.Thread.run(Thread.java:750)
```

## Maxwell's daemon

https://maxwells-daemon.io/  
https://github.com/zendesk/maxwell

貌似是一个应用程序，擅长把事件转成JSON，擅长结合MQ，例如Kafka

网上数据简单修改

```sql
CREATE TABLE `students` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    `age` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into students values(1,"小明",22);
update test set name='小红' where id=1;
delete from test where id=1;
```

```shell
{"database":"school","table":"students","type":"insert","ts":1552153502,"xid":832,"commit":true,"data":{"id":1,"name":"小明","age":22}}
{"database":"school","table":"students","type":"update","ts":1552153502,"xid":833,"commit":true,"data":{"id":1,"name":"小红","age":22},"old":{"name":"小明"}}
{"database":"school","table":"students","type":"delete","ts":1552153502,"xid":834,"commit":true,"data":{"id":1,"name":"小红","age":22}}
```

## Databus

https://github.com/linkedin/databus

貌似也是一个编译完就运行的程序

## Canal

https://github.com/alibaba/canal

canal  /kəˈnæl/ 运河；渠 管，道

貌似依赖zookeeper。貌似要下载启动canal服务端，然后再用客户端读取数据。