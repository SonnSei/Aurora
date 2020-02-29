# 吸烟者模型



```java
public class Test {

    public static void main(String[] args) {
        // 线程按顺序执行的这种，可以用下面这种模式，每个线程有自己的信号量，然后有一个finish信号量
        Semaphore semaphoreA = new Semaphore(0);
        Semaphore semaphoreB = new Semaphore(0);
        Semaphore semaphoreC = new Semaphore(0);
        Semaphore finish = new Semaphore(0);
        new Thread(new Printer(semaphoreA, finish, 'A')).start();
        new Thread(new Printer(semaphoreB, finish, 'B')).start();
        new Thread(new Printer(semaphoreC, finish, 'C')).start();
        new Thread(new Provider(semaphoreA, semaphoreB, semaphoreC, finish)).start();
    }

    static class Provider implements Runnable {
        Semaphore semaphoreA;
        Semaphore semaphoreB;
        Semaphore semaphoreC;
        Semaphore finish;

        public Provider(Semaphore semaphoreA, Semaphore semaphoreB, Semaphore semaphoreC, Semaphore finish) {
            this.semaphoreA = semaphoreA;
            this.semaphoreB = semaphoreB;
            this.semaphoreC = semaphoreC;
            this.finish = finish;
        }

        @Override
        public void run() {
            try {
                for (int i = 1; i <= 9; i++) {
                    switch (i % 3) {
                        case 1:
                            semaphoreA.release();
                            break;
                        case 2:
                            semaphoreB.release();
                            break;
                        default:
                            semaphoreC.release();
                    }
                    finish.acquire();
                }
            } catch (InterruptedException e) {

            }
        }
    }

    static class Printer implements Runnable {
        Semaphore semaphore;
        Semaphore finish;
        char value;

        public Printer(Semaphore semaphore, Semaphore finish, char value) {
            this.semaphore = semaphore;
            this.finish = finish;
            this.value = value;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 3; i++) {
                    semaphore.acquire();
                    System.out.println(value);
                    finish.release();
                }
            } catch (InterruptedException e) {

            }
        }
    }
}
```



