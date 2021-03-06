网络的分层是一个层层封装的含义

* * * 

Linux上查看IP：ifconfig/ip addr
ifconfig属于net-tool
ip addr属于iproute2

五类IP地址：
|分类|首位|作用|
|--|--|--|
|A|0|7+24|
|B|10|12+16|
|C|110|21+8|
|D|1110|多播组号28位|
|E|11110|留待后用27位|

无间路由：CIDR
只有网络号和主机号，不设网络分类

伴随CIDR出现：
广播地址：10.100.122.255
子网掩码：与IP地址按位and可得到网络号

动态路由：DHCP配合BOOTP（自举协议）
几个阶段：
1. DHCP discover
2. DHCP offer
3. 做出选择
4. 服务器确认
5. 续租（过期50%的时候）

* * * 

Hub集线器在物理层工作
交换机是二层设备

ARP遇到多交换机的时候可能出现回路

动态路由算法：
1. 距离矢量路由算法，基于Bellman-ford算法，两个问题
    1. 好消息传得快，坏消息传递慢
    2. 每次都要发送整个路由表
2. 链路状态路由算法：基于Dijkstra算法


# 动态路由协议：
## 1.基于链路状态路由算法的OSPF
OSPF（Open Shortest Path First，开放式最短路径优先）广泛应用在数据中心中的协议。由于主要用在数据中心内部，用于路由决策，因而称为内部网关协议（Interior Gateway Protocol，简称 **IGP**）。内部网关协议的重点就是找到最短的路径。在一个组织内部，路径最短往往最优。当然有时候 OSPF 可以发现多个最短的路径，可以在这多个路径中进行负载均衡，这常常被称为等价路由。

## 2. 基于距离矢量路由算法的 BGP



# UDP应用：
1. QUIC（全称 Quick UDP Internet Connections，快速 UDP 互联网连接）是google基于UDP改良的通信协议，其目的是降低网络通信的延迟，提供更好的用户互动体验。
2. RTMP是基于TCP的，但是TCP有队首等待（强顺序性），而且用户对过期用户也不感兴趣，所有很多应用都基于UDP实现了自己的直播协议
3. 实时游戏
