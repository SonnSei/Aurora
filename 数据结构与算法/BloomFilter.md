
>亲爱的姑娘，我并不想离开

# BloomFilter

**当BitMap依然需要空间优化的时候，可以使用布隆过滤器**

```java
/**
 * @Classname BloomFilter
 * @Description 布隆过滤器
 * @Date 2020/1/11 10:06
 * @Author SonnSei
 */
public class BloomFilter {
    BitMap bitMap;
    int size;

    public BloomFilter(int size) {
        this.size = size;
        bitMap = new BitMap(size);
    }

    public void add(int n) {
        bitMap.add(hash1(n));
        bitMap.add(hash2(n));
    }

    public boolean contains(int n) {
        return bitMap.contains(hash1(n)) && bitMap.contains(hash2(n));
    }

    // 多次哈希，我们认为n是从1开始的
    private int hash1(int n) {
        return (n*17)%size;
    }

    // 哈希函数随便选的，只是表达布隆过滤器的设计思路，重点并不放在hash函数的设计上
    private int hash2(int n) {
        return (n<<3)%size;
    }
}
```