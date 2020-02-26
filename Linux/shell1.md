[toc]



# 什么是shell

shell是命令解释器，用于解释用户对操作系统的操作。

shell把用户命令发给内核，再把内核返回的结果反馈出来。

shell有很多，可以```cat /etc/shells```查看，CentOS、Ubuntu的shell默认是bash

# LInux的启动过程

1. BIOS
2. MBR（主引导部分，判断是否是可引导的）
3. BootLoader(grub)：启动和引导内核的主要工具，确定内核版本
4. kernel：加载内核
5. systemd：系统初始化.1号进程，可以查看一下
6. shell



查看MBR：```dd if=/dev/dsa of=mbr.bin bs=446 count=1```

如果导出512字节，会看到最后有个55 aa（十六进制），用来表示这个引导是有效的

446-512之间是分区表



grub配置里面有引导哪个内核相关的东西



```top -p 1```查看1号进程，Ubuntu是systemd



# 怎样编写一个shell脚本

- UNIX的哲学：一条命令只做一件事情
- 为了组合命令和多次执行，一般保存文本文件
- 赋予该文件权限



如果要把 ```cd /var ; ls``` （分号表示顺序执行）写成一个脚本，要给脚本文件可读和可执行权限

如果命令比较长，用换行代替分号

把命令写进文件，然后```chmod u+x filename```，执行的时候可以用```bash filename```，如果用**./**表示用默认的shell

一般会在文件开头写一个声明的东西，**#!/bin/bash**，学名**Sha-Bang**



所以，一个shell脚本：

- Sha-Bang

- 命令

- #开头的注释

- chmod u+rx filename

- 执行命令

  - bash ./filename.sh：用bash，子进程
  - ./filename.sh：用sha-bang，子进程
  - source ./filename.sh：当前进程
  - .filename.sh：当前进程

  

# shell脚本的执行方式



就是上面的执行命令

# 内建命令和外部命令的区别



内建命令不需要创建子进程

外部命令需要创建





# 管道与管道符

- 管道和信号一样，也是进程通信的方式一样
- 匿名管道（管道符）是shell编程经常用到的通信工具
- 管道符是“|”，将前一个命令执行的结果传递给后一个进程



```cat | ps -f```会看到子进程，ps执行后退出，cat等待输入



# 子进程与子shell





# 重定向符号

- 一个大于：覆盖
- 两个大于：追加
- 2> ：错误重定向
- &>：无论正确错误，都输出

wc -l < filename，利用输入重定向统计词数

