

```java
public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        MySubject subject = new MySubject();
        subject.addObserver(new MyObserver());
        subject.addObserver(new MyObserver());
        subject.set();
        subject.notifyObservers();
    }

    static class MyObserver implements Observer{
        @Override
        public void update(Observable o, Object arg) {
            System.out.println("got it!");
        }
    }

    static class MySubject extends Observable{
        public void set(){
            setChanged();
        }
    }
}
```

