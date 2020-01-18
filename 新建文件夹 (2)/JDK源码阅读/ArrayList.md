@[toc]
# 1. 概述
ArrayList ，基于```[]```数组实现的，支持自动扩容的动态数组。

# 2. 类图

![ArrayList继承关系](https://img-blog.csdnimg.cn/20200112145826941.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9oZWxsb2tpbmdyYXkuYmxvZy5jc2RuLm5ldA==,size_16,color_FFFFFF,t_70)

# 3. 成员变量
**序列化id**
```java
private static final long serialVersionUID = 8683452581122892189L;
```
* * * 

**默认容量**
```java
private static final int DEFAULT_CAPACITY = 10;
```		
* * * 

**共享空数组实例**
```java
private static final Object[] EMPTY_ELEMENTDATA = {};
```
当一个正常的```ArrayList```移除最后一个元素后，其内部```elementData```将指向该共享数组
* * * 
**共享数组实例，给使用默认size的空数组实例用**
```java
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
```

如果创建时未指定容量，则创建的实例的```elementData```将首先指向该数组，直到添加第一个元素
* * * 

**存放数据的数组**
```java
transient Object[] elementData;
```

* * * 

**数据量**
```java
 private int size；
```

```elementData```中元素的数量
* * * 

**最大容量**
```java
 private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
```

尝试分配更大的内存的时候可能会触发```OutOfMemoryError: Requested array size exceeds VM limit```
* * * 

# 4. 构造器

**无参构造器**
```java
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }
```
默认容量是10，但是并不是在初始化的时候就申请了内存，而是一开始指向一个共享数组，直到添加第一个元素的时候再去分配内存

* * * 

**指定容量的构造器**
```java
     public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }
```
- 当参数大于0，就去正常的申请内存
- 当参数等于0，会指向```EMPTY_ELEMENTDATA```，注意，当正常的实例删除最后一个元素的时候，其```elementData```也会指向```EMPTY_ELEMENTDATA```
- 当参数小于0，则会排除异常

* * * 

**接受集合参数的构造器**
```java
    public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // replace with empty array.
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }
```
- 注意其中所说的```6262652```问题，因为```elementData```是```Object[]```类型的，当```c.toArray()```方法在1.8中是可能返回别的类型的，比如```Arrays```的内部类```ArrayList.toArray```其实是返回了一个```E[]```的类型，所以这就可能导致一个结果：你声明的是```Object```数组，但是往里面方```Object```的时候却会出错  

```java
	    public static void main(String[] args) {
	        Object[] list = Arrays.asList(1, 2, 3).toArray();
	        list[0] = new Object();
	    }
	    异常：Exception in thread "main" java.lang.ArrayStoreException: java.lang.Object
```

# 5. 添加元素

**①添加元素到末尾**
```java
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }
     
```
- 除了需要需要确认容量（可能导致扩容），剩下的就是简单的数组元素添加

* * * 
**看一下确认容量的逻辑**
```java
    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }
    private static int calculateCapacity(Object[] elementData, int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
    }
    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }
```
- 首先确认新的容量大小，如果是```DEFAULTCAPACITY_EMPTY_ELEMENTDATA```则表示尚未初始，新容量需要大于等于默认容量10
- 如果新容量超出当前```elementData```的容量，就需要扩容了

* * * 
**扩容逻辑**
```java
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
```
- 首先对旧的容量扩容一半
- 如果扩容后仍小于所需容量，则直接把容量设置成所需容量
- 如果容量大于最大容量，则需要特殊处理
- 数据拷贝

* * * 
**对巨大容量的特殊处理**
```java
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
```
- 如果移除了就抛出```OOM```
- 否则就在```Integer.MAX_VALUE ```和```MAX_ARRAY_SIZE```中挑一个能满足要求的

**②添加元素到指定位置**
```java
public void add(int index, E element) {
    rangeCheckForAdd(index);

    ensureCapacityInternal(size + 1);  // Increments modCount!!
    System.arraycopy(elementData, index, elementData, index + 1,
                     size - index);
    elementData[index] = element;
    size++;
}
```
* * * 
看一下这个```System.arraycopy```

```java
public static native void arraycopy(
	Object src,  int  srcPos, Object dest, int destPos, int length);
```
就是从```index```开始往后的元素都右移一位

# 6. 批量添加元素

**①追加到末尾**

```java
public boolean addAll(Collection<? extends E> c) {
    Object[] a = c.toArray();
    int numNew = a.length;
    ensureCapacityInternal(size + numNew);  // Increments modCount
    System.arraycopy(a, 0, elementData, size, numNew);
    size += numNew;
    return numNew != 0;
}
```

**②插入到指定位置**

```java
public boolean addAll(int index, Collection<? extends E> c) {
    rangeCheckForAdd(index);

    Object[] a = c.toArray();
    int numNew = a.length;
    ensureCapacityInternal(size + numNew);  // Increments modCount

    int numMoved = size - index;
    if (numMoved > 0)
        System.arraycopy(elementData, index, elementData, index + numNew,
                         numMoved);

    System.arraycopy(a, 0, elementData, index, numNew);
    size += numNew;
    return numNew != 0;
}
```
