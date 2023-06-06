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
