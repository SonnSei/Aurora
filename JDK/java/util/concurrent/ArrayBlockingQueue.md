# ArrayBlockingQueue

[toc]

## 1. 掌握用Lock和Condition构建阻塞队列，注意其中的注释

```java
 class BoundedBuffer {
     // 1. 注意这个final，因为锁指针一旦变了，锁就失效了
     final Lock lock = new ReentrantLock();
     final Condition notFull = lock.newCondition();
     final Condition notEmpty = lock.newCondition();

     final Object[] items = new Object[100];
     int putptr, takeptr, count;

     public void put(Object x) throws InterruptedException {
         // 2. 注意这个范式：上锁-紧接着try-业务代码-finally里面释放锁
         lock.lock();
         try {
             // 3. MESA模型的标志性写法，在while循环里await，COndition的await方法是会释放锁的
             while (count == items.length)
                 notFull.await();
             items[putptr] = x;
             if (++putptr == items.length) putptr = 0;
             ++count;
             notEmpty.signal();
         } finally {
             lock.unlock();
         }
     }

     public Object take() throws InterruptedException {
         lock.lock();
         try {
             while (count == 0)
                 notEmpty.await();
             Object x = items[takeptr];
             if (++takeptr == items.length) takeptr = 0;
             --count;
             notFull.signal();
             // 4. return可以放在这里
             return x;
         } finally {
             lock.unlock();
         }
     }
 }
```



## 2. 非公平锁在该类里的作用

指的是Lock锁是非公平的，也就是生产者线程和消费者线程之间的竞争是不公平的。



## 3. 几种入队、出队方法的比较

|             |  抛异常   | 返回特殊值 |  阻塞  |        超时        |
| :---------: | :-------: | :--------: | :----: | :----------------: |
| **Insert**  |  add(e)   |  offer(e)  | put(e) | offer(e,time,unit) |
| **remove**  | remove()  |   poll()   | take() |  poll(time,unit)   |
| **examine** | element() |   peek()   |   -    |         -          |



## 4. 循环数组实现的队列了解一下

## 5.是需要确定容量的

## 6. 这里是没有ConcurrentModificationException的

# 7. 它的迭代器有的东西

