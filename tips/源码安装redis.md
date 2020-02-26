1. 解压
2. 进入目录执行make，此过程遇到的问题：
    1. 没有安装GCC
    2. jemalloc/jemalloc.h：没有那个文件或目录--》上次编译有残留，运行 make distclean
3. make test，此过程可能需要安装tcl
4. make install 装入/usr/local/bin