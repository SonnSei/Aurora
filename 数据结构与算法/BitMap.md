# BitMap

>BitMap的适用场景是：大量数据，只需要```add```和```contains```操作
>实现思路也非常简单，就是每一bit代表一条数据

```java
/**
 * @Classname Bitmap
 * @Description TODO
 * @Date 2020/1/11 8:55
 * @Author SonnSei
 */
public class BitMap {
    public static void main(String[] args) {
        BitMap bitMap = new BitMap(10);
        bitMap.add(2);
        System.out.println(bitMap.contains(2));
        System.out.println(bitMap.contains(3));
    }
    // Java中char类型占16bit，也即是2个字节
    private char[] bytes;
    private int size;

    // 最多装载n个元素
    public BitMap(int n) {
        size = n;
        this.bytes = new char[n / 16 + 1];
    }

    public void add(int k) {
        if (k > size) return;
        int charIndex = k / 16;
        int bitIndex = k % 16;
        bytes[charIndex] |= (1 << bitIndex);
    }

    public boolean contains(int k) {
        if (k > size) return false;
        int charIndex = k / 16;
        int bitIndex = k % 16;
        return (bytes[charIndex] & (1 << bitIndex)) != 0;
    }
}
```

