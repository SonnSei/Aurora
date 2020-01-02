package java.lang;

public final class Byte extends Number implements Comparable<Byte> {

    // 最大值最小值
    public static final byte   MIN_VALUE = -128;
    public static final byte   MAX_VALUE = 127;

    
    @SuppressWarnings("unchecked")
    public static final Class<Byte>     TYPE = (Class<Byte>) Class.getPrimitiveClass("byte");

    // 转10进制int之后调用的Integer的toString方法
    public static String toString(byte b) {
        return Integer.toString((int)b, 10);
    }

    // -128-127的数据缓存
    private static class ByteCache {
        private ByteCache(){}

        static final Byte cache[] = new Byte[-(-128) + 127 + 1];

        static {
            for(int i = 0; i < cache.length; i++)
                cache[i] = new Byte((byte)(i - 128));
        }
    }

     // byte的所有数值都是在缓存里的
     // since 1.5
    public static Byte valueOf(byte b) {
        final int offset = 128;
        return ByteCache.cache[(int)b + offset];
    }

    // 调用的Integer的parseInt之后判断是否在Byte的允许范围内
    public static byte parseByte(String s, int radix)
        throws NumberFormatException {
        int i = Integer.parseInt(s, radix);
        if (i < MIN_VALUE || i > MAX_VALUE)
            throw new NumberFormatException(
                "Value out of range. Value:\"" + s + "\" Radix:" + radix);
        return (byte)i;
    }
    // 默认进制10
    public static byte parseByte(String s) throws NumberFormatException {
        return parseByte(s, 10);
    }

    
    public static Byte valueOf(String s, int radix)
        throws NumberFormatException {
        return valueOf(parseByte(s, radix));
    }
    public static Byte valueOf(String s) throws NumberFormatException {
        return valueOf(s, 10);
    }

    // 与正常的valueOf相比，多了对不同进制字符串的处理，比如0X开头等等
    public static Byte decode(String nm) throws NumberFormatException {
        int i = Integer.decode(nm);
        if (i < MIN_VALUE || i > MAX_VALUE)
            throw new NumberFormatException(
                    "Value " + i + " out of range from input " + nm);
        return valueOf((byte)i);
    }

    private final byte value;

    // 构造器
    public Byte(byte value) {
        this.value = value;
    }

    // 默认是10进制的，不能接受别的进制的字符串
    public Byte(String s) throws NumberFormatException {
        this.value = parseByte(s, 10);
    }

    // 下面一组方法转 byte、short、int、long、float、double
    // 毕竟byte是最短的，向上转不会损失精度
    public byte byteValue() {return value;}
    public short shortValue() {return (short)value;}    
    public int intValue() {return (int)value;}
    public long longValue() {return (long)value;}    
    public float floatValue() {return (float)value;}    
    public double doubleValue() {return (double)value;}

    // 这个是实例方法，还有一个toString的类方法
    public String toString() {
        return Integer.toString((int)value);
    }

    // 哈希值直接用的数值，1.7里面是直接返回的(int)value，1.8里面把它弄成了类方法，然后调用
    @Override
    public int hashCode() {
        return Byte.hashCode(value);
    }
    public static int hashCode(byte value) {
        return (int)value;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Byte) {
            return value == ((Byte)obj).byteValue();
        }
        return false;
    }

    // compare比较的是内部的基本数据类型value
    public int compareTo(Byte anotherByte) {
        return compare(this.value, anotherByte.value);
    }
    // 静态方法
    public static int compare(byte x, byte y) {
        return x - y;
    }

    // since 1.8
    // 可以记一下这个写法，转int的是是高位补最高
    // 比如byte的3的二进制是00000011，转int的时候高位补的是0
    //    byte的-1的二进制是11111111，转int的时候高位补的是1
    // &0xff之后相当于只取后1字节，也就是byte的长度
    public static int toUnsignedInt(byte x) {
        return ((int) x) & 0xff;
    }
    public static long toUnsignedLong(byte x) {
        return ((long) x) & 0xffL;
    }

    public static final int SIZE = 8;    
    // 字节数
    // Character是2个字节
    public static final int BYTES = SIZE / Byte.SIZE;

    private static final long serialVersionUID = -7183698231559129828L;
}
