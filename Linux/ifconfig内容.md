# ip addr 命令的内容

```
[root@iZuf6fkf5wr6o5cfzpeakiZ home]# ip addr
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
2: eth0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP group default qlen 1000
    link/ether 00:16:3e:04:c3:78 brd ff:ff:ff:ff:ff:ff
    inet 172.19.123.21/20 brd 172.19.127.255 scope global dynamic eth0
       valid_lft 313979742sec preferred_lft 313979742sec
```

- ```lo```:全称是 loopback，又称环回接口，往往会被分配到 127.0.0.1 这个地址。这个地址用于本机通信，经过内核处理后直接返回，不会在任何网络中出现。
- ```net_device flags```:<BROADCAST,MULTICAST,UP,LOWER_UP>
	- **BROADCAST** 表示这个网卡有广播地址，可以发送广播包；
	- **MULTICAST** 表示网卡可以发送多播包；
	- **UP** 表示网卡处于启动的状态；
	- **LOWER_UP** 表示 L1 是启动的，也即网线插着呢
- ```mtu```:最大传输单元（Maximum Transmission Unit，缩写MTU）
- ```qdisc pfifo_fast```:**queueing discipline**，中文叫**排队规则**,内核如果需要通过某个网络接口发送数据包，它都需要按照为这个接口配置的 qdisc（排队规则）把数据包加入队列。
	- 最简单的 qdisc 是 **pfifo**，它不对进入的数据包做任何的处理，数据包采用先入先出的方式通过队列。
	- **pfifo_fast** 稍微复杂一些，它的队列包括三个波段（band）。在每个波段里面，使用先进先出规则。三个波段（band）的优先级也不相同。band 0 的优先级最高，band 2 的最低。如果 band 0 里面有数据包，系统就不会处理 band 1 里面的数据包，band 1 和 band 2 之间也是一样。