---
layout: post
title: "redolog与binlog"
description: "innodb的日志相关"
categories: [知识总结]
tags: [MySQL]
redirect_from:
  - /2020/01/18/
---

**bin log是InnoDB加入MySQL之前就有的，主要用来做备份用的**

**redo log是InnoDB加入MySQL之后才有的，为的是提供crash safe的功能，也就是提供崩溃恢复**

# 1. bin log

bin log就是用来备份的日志，万一哪天数据库被删了，可以通过它进行恢复

bin log是server层的，任何引擎都可以通过它恢复数据（row和statement两种形式）



# 2. redo log

redo log是放在磁盘中的一块区域，用于记录一定数量的操作，在MySQL异常重启的时候，可以读取这部分数据来进行数据恢复。

bin log是搜索引擎层的，只属于InnoDB

>既然需要写到磁盘，那和bin log有什么不同？

bin log的目的是无差别的记录操作以达到备份的目的，借助bin log，数据库甚至可以恢复至半年前的状态。而redo log是记录那些**已经在内存中做过了处理，但是没有持久化到硬盘**的操作

>那既然redo log也要写到磁盘，为什么不直接做持久化呢？何必多此一举？

关键就在于redo log的磁盘空间是事先分配好的一块连续区间，顺序的读写速度勉强可以接受。而写bin log就属于随机存取了，速度就无法接受了

# 3. 两段式提交

>两段式提交的过程  

![两段式提交](../../../../images/dataBase/twoPhaseSubmit.png)

>有什么意义？

目的就是保证redo log和bin log的一致性

**如果不用两段式提交：**
1. 先写 redo log 后写 binlog。假设在 redo log 写完，binlog 还没有写完的时候，MySQL 进程异常重启。由于我们前面说过的，redo log 写完之后，系统即使崩溃，仍然能够把数据恢复回来，所以恢复后这一行 c 的值是 1。但是由于 binlog 没写完就 crash 了，这时候 binlog 里面就没有记录这个语句。因此，之后备份日志的时候，存起来的 binlog 里面就没有这条语句。然后你会发现，如果需要用这个 binlog 来恢复临时库的话，由于这个语句的 binlog 丢失，这个临时库就会少了这一次更新，恢复出来的这一行 c 的值就是 0，与原库的值不同。

2. 先写 binlog 后写 redo log。如果在 binlog 写完之后 crash，由于 redo log 还没写，崩溃恢复以后这个事务无效，所以这一行 c 的值是 0。但是 binlog 里面已经记录了“把 c 从 0 改成 1”这个日志。所以，在之后用 binlog 来恢复的时候就多了一个事务出来，恢复出来的这一行 c 的值就是 1，与原库的值不同。


# 补充
- 崩溃恢复的作用是恢复尚未提交的事务的数据，因为事务提交之后，就必须是持久化的了，就已经不允许丢失了
- **innodb_flush_log_at_trx_commit** 这个参数设置成 1 的时候，表示每次事务的 redo log 都直接持久化到磁盘。这个参数建议设置成 1，这样可以保证 MySQL 异常重启之后数据不丢失。

- **sync_binlog** 这个参数设置成 1 的时候，表示每次事务的 binlog 都持久化到磁盘。这个参数也建议设置成 1，这样可以保证 MySQL 异常重启之后 binlog 不丢失。

- 误删之后的，不能只靠binlog来做，因为业务逻辑可能因为误删操作的行为，插入了逻辑错误的语句，
- 更新一条数据需要三次访问磁盘：1. redo log prepare; 2. bin log 写; 3. redo log 完成