# 1. 继承关系图

# 2. 成员变量

```java
private volatile Interruptible blocker;
private final Object blockerLock = new Object();
// 该线程的上下文加载器
private ClassLoader contextClassLoader;

// 是否是守护线程
private boolean     daemon = false;

private static volatile UncaughtExceptionHandler defaultUncaughtExceptionHandler;
```